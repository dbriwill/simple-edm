package fr.simple.ged.mapper;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
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

	@SuppressWarnings("unchecked")
	public List<T> dtoToBo(List<S> dtos) {
		return (List<T>) CollectionUtils.transformedCollection(dtos, new Transformer() {
			@Override
			public Object transform(Object arg0) {
				return dtoToBo((S) arg0);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<S> boToDto(List<T> bos) {
		return (List<S>) CollectionUtils.transformedCollection(bos, new Transformer() {
			@Override
			public Object transform(Object arg0) {
				return boToDto((T) arg0);
			}
		});
	}
}
