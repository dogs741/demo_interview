package com.example.demo.mapper;

import com.example.demo.po.Station;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StationMapper {
    @Select("SELECT * FROM `demo`.`station`")
    List<Station> getAllStations();
}
