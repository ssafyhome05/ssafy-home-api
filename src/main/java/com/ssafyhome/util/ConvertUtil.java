package com.ssafyhome.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConvertUtil {

    private final ModelMapper modelMapper;

    public ConvertUtil(ModelMapper modelMapper) {

        this.modelMapper = modelMapper;
    }

    public <D, T> D convert(T t, Class<D> dClass) {

        return modelMapper.map(t, dClass);
    }

    public <D, T> List<D> convert(List<T> tList, Class<D> dClass) {

        return tList.stream()
                .map(t -> modelMapper.map(t, dClass))
                .toList();
    }
}
