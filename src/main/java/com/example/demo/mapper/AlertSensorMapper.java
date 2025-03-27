package com.example.demo.mapper;

import com.example.demo.enums.AquarkSensorEnum;
import com.example.demo.po.AlertSensor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AlertSensorMapper {
    @Insert("INSERT INTO `demo`.`alert_sensor`(sensor_name, threshold) VALUES(#{alert.sensorName}, #{alert.threshold}) ON DUPLICATE KEY UPDATE threshold = #{alert.threshold}")
    int insert(@Param("alert") AlertSensor alertSensor);

    @Select("SELECT * FROM `demo`.`alert_sensor` WHERE sensor_name = #{name}")
    AlertSensor getLatestRecordBySensorName(@Param("name") AquarkSensorEnum sensorName);

    @Select("SELECT * FROM `demo`.`alert_sensor`")
    List<AlertSensor> getAllRecord();
}
