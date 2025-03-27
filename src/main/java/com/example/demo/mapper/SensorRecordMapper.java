package com.example.demo.mapper;

import com.example.demo.po.SensorRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SensorRecordMapper {

    @Insert("""
            <script>
                INSERT INTO `demo`.`sensor_record`(
                    `station_id`,
                    `obs_time`,
                    `csq`,
                    `rain_d`,
                    `rh`,
                    `tx`,
                    `echo`,
                    `speed`,
                    `v1`,
                    `v2`,
                    `v3`,
                    `v4`,
                    `v5`,
                    `v6`,
                    `v7`
                )
                VALUES
                <foreach collection="sensorList" item="item" open="" close="" separator=",">
                (
                    #{item.stationId},
                    #{item.obsTime},
                    #{item.csq},
                    #{item.rainD},
                    #{item.rh},
                    #{item.tx},
                    #{item.echo},
                    #{item.speed},
                    #{item.v1},
                    #{item.v2},
                    #{item.v3},
                    #{item.v4},
                    #{item.v5},
                    #{item.v6},
                    #{item.v7}
                )
                </foreach>
            </script>
            """)
    int batchInsert(@Param("sensorList") List<SensorRecord> sensorRecordList);

    @Select("")
    List<SensorRecord> getAllRecords();

    @Select("""
            <script>
                SELECT * FROM `sensor_record` WHERE station_id IN
                <foreach collection="stationIds" item="item" open="(" close=")" separator=",">
                    #{item.stationId}
                </foreach>
                AND obs_time BETWEEN #{startDate} AND #{endDate}
            </script>
            """)
    List<SensorRecord> getRecordsByArguments(@Param("stationIds") List<String> stationIds,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
}
