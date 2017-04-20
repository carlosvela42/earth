package co.jp.nej.earth.service;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.MgrMenu;

public interface MenuService {

    List<MgrMenu> getMenuByProfileId(List<String> profileIds) throws EarthException;
}