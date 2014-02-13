package fr.simple.edm.mapper;

import java.util.ArrayList;
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

	/**
	 * Try to map the DTO to BO, returns null if failed
	 */
	@SuppressWarnings("unchecked")
    public <U> T dtoToBoOrNull(U potentialDto) {
	    try {
    	    if (! potentialDto.getClass().equals(ss.newInstance().getClass())) {
    	        return null;
    	    }
    	    return dtoToBo((S) potentialDto);
	    }
	    catch(Exception e) {
	        return null;
	    }
	}
	
	/**
     * Try to map the DTO to BO, returns null if failed
     */
    @SuppressWarnings("unchecked")
    public <U> S boToDtoOrNull(U potentialBo) {
        System.err.println("p " + potentialBo.getClass());
        System.err.println("t " + tt.getClass());
        try {
            if (! potentialBo.getClass().equals(tt.newInstance().getClass())) {
                return null;
            }
            return boToDto((T) potentialBo);
        }
        catch(Exception e) {
            return null;
        }
    }
	
	@SuppressWarnings("unchecked")
	public List<T> dtoToBo(List<S> dtos) {
		return new ArrayList<T>(CollectionUtils.transformedCollection(dtos, new Transformer() {
			@Override
			public Object transform(Object arg0) {
				return dtoToBo((S) arg0);
			}
		}));
	}
	
	@SuppressWarnings("unchecked")
	public List<S> boToDto(List<T> bos) {
		return new ArrayList<S>(CollectionUtils.transformedCollection(bos, new Transformer() {
			@Override
			public Object transform(Object arg0) {
				return boToDto((T) arg0);
			}
		}));
	}
}
