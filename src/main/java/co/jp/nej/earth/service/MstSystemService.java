package co.jp.nej.earth.service;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;

public interface MstSystemService {
	public List<MstSystem> getMstSystem() throws EarthException;
}
