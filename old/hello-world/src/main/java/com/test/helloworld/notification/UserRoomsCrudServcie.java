package com.test.helloworld.notification;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient.GenericExecuteSpec;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.helloworld.notification.domain.Rooms;
import com.test.helloworld.notification.domain.UserRooms;
import com.test.helloworld.notification.domain.Users;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
// https://github.com/hantsy/spring-r2dbc-sample/blob/master/database-client/src/main/java/com/example/demo/PostRepository.java#L59
@Service
public class UserRoomsCrudServcie {
	public static final String TABLE_NAME = "user_rooms";

	public static final String SQL_SELECT_ALL_ROOMS = "SELECT r.* FROM " + TABLE_NAME + " r ";
	public static final String SQL_COUNT_ALL_ROOMS = "SELECT COUNT(r.*) FROM " + TABLE_NAME + " r ";
	public static final String SQL_SELECT_USER_ROOMS = "SELECT r.* FROM " + TABLE_NAME + " r WHERE r.user_id = :userId";
	public static final String SQL_SELECT_ROOMS_BY_USER = "SELECT rm.* FROM " + TABLE_NAME + " r LEFT JOIN Rooms rm ON rm.id = r.room_id WHERE r.user_id = :userId";
	public static final String SQL_COUNT_USER_ROOMS = "SELECT COUNT(r.*) FROM " + TABLE_NAME + " r WHERE r.user_id = :userId";
	public static final String SQL_SELECT_ROOM_MEMBERS = "SELECT r.* FROM " + TABLE_NAME + " r WHERE r.room_id = :roomId";
	public static final String SQL_SELECT_ROOM_MEMBERS_AS_USERS = "SELECT u.* FROM " + TABLE_NAME + " r LEFT JOIN users u ON u.id = r.user_id WHERE r.room_id = :roomId";
	public static final String SQL_COUNT_ROOM_MEMBERS = "SELECT COUNT(r.*) FROM " + TABLE_NAME + " r WHERE r.room_id = :roomId";
	
	public static final String SQL_SELECT_ROOM = "SELECT r.* FROM " + TABLE_NAME + " r WHERE r.room_id = :roomId AND r.user_id = :userId";
	public static final String SQL_INSERT_ROOM = "INSERT INTO " + TABLE_NAME + "(room_id, user_id) VALUES (:roomId, :userId)";
	//public static final String SQL_UPDATE_ROOM = "SELECT r.* FROM " + TABLE_NAME + " r WHERE r.room_id = :roomId AND r.user_id = :userId";
	public static final String SQL_DELETE_BY_USER_ROOM = "DELETE FROM " + TABLE_NAME + " r WHERE r.room_id = :roomId AND r.user_id = :userId";
	public static final String SQL_DELETE_BY_USER = "DELETE FROM " + TABLE_NAME + " r WHERE r.user_id = :userId";
	public static final String SQL_DELETE_BY_ROOM = "DELETE FROM " + TABLE_NAME + " r WHERE r.room_id = :roomId";

	private static final String SQL_PAGINATION = " LIMIT :_lim OFFSET :_off";
	
	 R2dbcEntityTemplate template;
	 RoomRepository roomRepository;
	 
	 BiFunction<Row, RowMetadata, UserRooms> getMapper() {
		 return new UserRoomsRowMapper();
	 }
	 
	 BiFunction<Row, RowMetadata, Rooms> getRoomMapper() {
		 return new RoomsRowMapper();
	 }
	 
	 BiFunction<Row, RowMetadata, Users> getUserMapper() {
		 return new UserRowMapper();
	 }

	@Autowired
	public UserRoomsCrudServcie(R2dbcEntityTemplate template, RoomRepository roomRepository) {
		super();
		this.template = template;
		this.roomRepository = roomRepository;
	}
	
	private void insert() {
		template.getDatabaseClient()
			.sql("")
			.filter((statement, executeFunction) -> statement.returnGeneratedValues("id").execute())
			.map(r -> r.get("id", Long.class)).first();
	}		
	
	@Transactional
	public Mono<Rooms> createRoom(String name) {
		return roomRepository
				.save(new Rooms(null, name));
	}
	
	@Transactional
	public Mono<Void> deleteRoom(Integer id) {
		return deleteByRoomId(id)
				.then(roomRepository.deleteById(id))
				.then();
	}
	 
	private GenericExecuteSpec bindPagination(
			GenericExecuteSpec sql, 
			Pageable pageable) {
		return sql.bind("_lim", pageable.getPageSize())
				.bind("_off",pageable.getOffset());
	}
	
	public Flux<UserRooms> findAll() {
		return template.getDatabaseClient()
		.sql(SQL_SELECT_ALL_ROOMS)
		.map(getMapper())
		.all();
	}
	
	public Mono<Long> count() {
		return template.getDatabaseClient()
		.sql(SQL_COUNT_ALL_ROOMS)
		.map(m -> m.get(0, Long.class))
		.one();
	}
	
	public Flux<UserRooms> findAll(Pageable pageable) {
		return 
		bindPagination(
			template.getDatabaseClient()
			.sql(SQL_SELECT_ALL_ROOMS + SQL_PAGINATION),
			pageable
		)	.filter(t -> t.fetchSize(10))
		.map(getMapper())
		.all();
	}
	
	public Flux<UserRooms> findByUserId(Long userId) {
		return template.getDatabaseClient()
		.sql(SQL_SELECT_USER_ROOMS)
		.bind("userId", userId)
		.map(getMapper())
		.all();
	}
	
	public Flux<Long> countByUserId() {
		return template.getDatabaseClient()
		.inConnection(
			c -> Mono.just(c)
		).flatMapMany(c-> c.createStatement(SQL_COUNT_ALL_ROOMS).returnGeneratedValues("id").execute())
		.flatMap(d -> d.map(m -> m.get("id", Long.class)));
	}
	
	public Flux<UserRooms> findByUserId(Long userId, Pageable pageable) {
		return bindPagination(
				template.getDatabaseClient()
				.sql(SQL_SELECT_USER_ROOMS + SQL_PAGINATION),
				pageable
			)
		.bind("userId", userId)
			.filter(t -> t.fetchSize(10))
		.map(getMapper())
		.all();
	}
	
	public Flux<Rooms> findRoomByUserId(Long userId, Pageable pageable) {
		return bindPagination(
				template.getDatabaseClient()
				.sql(SQL_SELECT_ROOMS_BY_USER + SQL_PAGINATION),
				pageable
			)
		.bind("userId", userId)
			.filter(t -> t.fetchSize(10))
		.map(getRoomMapper())
		.all();
	}
	
	public Mono<Long> countByUserId(Long userId) {
		return template.getDatabaseClient()
		.sql(SQL_COUNT_USER_ROOMS)
		.bind("userId", userId)
		.map(m -> m.get(0, Long.class))
		.one();
	}
	
	public Flux<UserRooms> findByRoomId(Integer roomId) {
		return template.getDatabaseClient()
		.sql(SQL_SELECT_ROOM_MEMBERS)
		.bind("roomId", roomId)
		.map(getMapper())
		.all();
	}
	
	public Flux<Users> findMembersByRoomId(Integer roomId, Pageable pageable) {
		return bindPagination(
				template.getDatabaseClient()
				.sql(SQL_SELECT_ROOM_MEMBERS_AS_USERS + SQL_PAGINATION),
				pageable
			)
		.bind("roomId", roomId)
			.filter(t -> t.fetchSize(10))
		.map(getUserMapper())
		.all();
	}
	

	public Mono<Long> countByRoomId(Integer roomId) {
		return template.getDatabaseClient()
		.sql(SQL_COUNT_ROOM_MEMBERS)
		.bind("roomId", roomId)
		.map(m -> m.get(0, Long.class))
		.one();
	}
	
	public Mono<UserRooms> findById(Integer roomId, Long userId) {
		return template.getDatabaseClient()
		.sql(SQL_SELECT_ROOM)
		.bind("userId", userId)
		.bind("roomId", roomId)
		.map(getMapper())
		.one();
	}
	
	@Transactional
	public Mono<Long> insert(UserRooms userRoom) {
		return template.getDatabaseClient()
		.sql(SQL_INSERT_ROOM)
		.bind("userId", userRoom.getUserId())
		.bind("roomId", userRoom.getRoomId())
		.fetch()
	    .rowsUpdated();
	}
	
	@Transactional
	public Mono<Long> deleteById(Integer roomId, Long userId) {
		return template.getDatabaseClient()
		.sql(SQL_DELETE_BY_USER_ROOM)
		.bind("userId", userId)
		.bind("roomId", roomId)
		.fetch()
	      .rowsUpdated();
	}
	@Transactional
	public Mono<Long> deleteByUserId(Long userId) {
		return template.getDatabaseClient()
		.sql(SQL_DELETE_BY_USER)
		.bind("userId", userId)
		.fetch()
	      .rowsUpdated();
	}
	
	@Transactional
	public Mono<Long> deleteByRoomId(Integer roomId) {
		return template.getDatabaseClient()
		.sql(SQL_DELETE_BY_ROOM)
		.bind("roomId", roomId)
		.fetch()
	      .rowsUpdated();
	}
	
	public static class UserRoomsRowMapper implements BiFunction<Row, RowMetadata, UserRooms>  {

		public static final UserRoomsRowMapper INSTANCE = new UserRoomsRowMapper();
		
		@Override
		public UserRooms apply(Row t, RowMetadata u) {
			return new UserRooms(
					t.get("room_id", Integer.class),
					t.get("user_id", Long.class)
			);
		}
	}
	
	public static class RoomsRowMapper implements BiFunction<Row, RowMetadata, Rooms>  {

		public static final RoomsRowMapper INSTANCE = new RoomsRowMapper();
		
		@Override
		public Rooms apply(Row t, RowMetadata u) {
			return new Rooms(
					t.get("id", Integer.class),
					t.get("name", String.class)
			);
		}
	}
	
	public static class UserRowMapper implements BiFunction<Row, RowMetadata, Users>  {

		public static final UserRowMapper INSTANCE = new UserRowMapper();
		
		@Override
		public Users apply(Row t, RowMetadata u) {
						
			return new Users(
					t.get("id", Long.class),
					t.get("name", String.class),
					t.get("responded_at", Instant.class),
						//.toInstant(ZoneOffset.UTC),
					t.get("last_msg_at", Instant.class)
						//.toInstant(ZoneOffset.UTC)
			);
		}
	}


}
