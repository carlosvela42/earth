package co.jp.nej.earth.id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Site;
import co.jp.nej.earth.service.SiteService;

@Service
public class ESiteIdImpl implements ESiteId {

  @Autowired
  private SiteService siteService;

  @Override
  public int getAutoId() throws EarthException {
    List<Integer> siteIds = new ArrayList<>();
    List<Site> sites = siteService.getAllSites();
    if (sites.size() > 0) {
      for (Site temp : sites) {
        siteIds.add(temp.getSiteId());
      }
      return Collections.max(siteIds) + 1;
    } else {
      return 1;
    }
  }

}
