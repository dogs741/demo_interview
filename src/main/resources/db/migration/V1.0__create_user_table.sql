
CREATE TABLE IF NOT EXISTS demo.sensor_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    station_id VARCHAR(30) NOT NULL COMMENT 'station_id',
    obs_time TIMESTAMP(3) NOT NULL,
    csq VARCHAR(5) NOT NULL COMMENT 'csq level',
    rain_d DECIMAL(7,3),
    rh DECIMAL(7,3),
    tx DECIMAL(7,3),
    echo DECIMAL(7,3),
    speed DECIMAL(7,3),
    v1 DECIMAL(7,3),
    v2 DECIMAL(7,3),
    v3 DECIMAL(7,3),
    v4 DECIMAL(7,3),
    v5 DECIMAL(7,3),
    v6 DECIMAL(7,3),
    v7 DECIMAL(7,3),
    create_date TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_date TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id),
    KEY station_id_obs_time (station_id, obs_time)
) COMMENT 'sensor_record';

CREATE TABLE IF NOT EXISTS demo.alert_sensor (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    sensor_name VARCHAR(30) NOT NULL COMMENT 'sensor name',
    threshold DECIMAL(7,3) NOT NULL COMMENT 'sensor\'s threshold for alert',
    create_date TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_date TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id),
    UNIQUE KEY sensor_name (sensor_name)
) COMMENT 'alert_sensor';

CREATE TABLE IF NOT EXISTS demo.station (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    station_id VARCHAR(30) NOT NULL COMMENT 'station_id',
    station_name VARCHAR(30) NOT NULL COMMENT 'station name',
    create_date TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_date TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id)
) COMMENT 'station';

INSERT INTO station(station_id, station_name) VALUES
('240627', 'TEST1'),
('240706', 'TEST2'),
('240708', 'TEST3'),
('240709', 'TEST4'),
('240710', 'TEST5');

