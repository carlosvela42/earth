package co.jp.nej.earth.service;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MstSystem;

public interface MstSystemService {
    List<MstSystem> getMstSystem() throws EarthException;

    List<MstSystem> getMstSystemBySection(String section) throws EarthException;
}
