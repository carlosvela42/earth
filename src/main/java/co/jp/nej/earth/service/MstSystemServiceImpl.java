package co.jp.nej.earth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.jp.nej.earth.dao.MstSystemDao;
import co.jp.nej.earth.exception.EarthException;

@Transactional
@Service
public class MstSystemServiceImpl implements MstSystemService {

	@Autowired
	private MstSystemDao mstSystemDao;

	public List<MstSystem> getMstSystem() throws EarthException {
		return mstSystemDao.getMstSystem();
	}

}
