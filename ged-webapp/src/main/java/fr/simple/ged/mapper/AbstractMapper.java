package fr.simple.ged.mapper;

import org.springframework.beans.BeanUtils;

public class AbstractMapper<T, S> {

	private Class<T> tt;
	private Class<S> ss;
	
	public AbstractMapper(Class<T> model, Class<S> dto) {
		tt = model;
		ss = dto;
	}
	
	public T dtoToBo(S dto) {
		T t = null;
		try {
			t = tt.newInstance();
			BeanUtils.copyProperties(dto, t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public S boToDto(T bo) {
		S s = null;
		try {
			s = ss.newInstance();
			BeanUtils.copyProperties(bo, s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

}
