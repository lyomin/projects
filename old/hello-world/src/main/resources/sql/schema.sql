CREATE TABLE IF NOT EXISTS rooms (
	id SERIAL PRIMARY KEY,
	name VARCHAR (100)
);

CREATE TABLE IF NOT EXISTS users (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR (100),
	responded_At timestamp with time zone,
	last_Msg_At timestamp with time zone
);

CREATE TABLE IF NOT EXISTS messages (
	id BIGSERIAL PRIMARY KEY,
	room_id INT NOT NULL,
	message VARCHAR (500),
	created_at timestamp with time zone,
	updated_at timestamp with time zone,
	CONSTRAINT fk_m_rooms_id
    	  FOREIGN KEY(room_id) 
	    REFERENCES rooms(id)
);

CREATE TABLE IF NOT EXISTS user_rooms (
	room_id INT NOT NULL,
	user_id BIGINT NOT NULL,
	PRIMARY KEY (room_id, user_id),
	CONSTRAINT fk_ur_rooms_id
    	  FOREIGN KEY(room_id) 
	    REFERENCES rooms(id),
	CONSTRAINT fk_ur_user_id
    	  FOREIGN KEY(user_id) 
	    REFERENCES users(id)
);
