package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.service.MstSystem;

public interface MstSystemDao {
	public List<MstSystem> getMstSystem() throws EarthException;

}
