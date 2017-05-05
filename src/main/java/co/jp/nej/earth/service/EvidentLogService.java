package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.StrLogAccess;
import com.querydsl.core.types.OrderSpecifier;

import java.util.List;

public interface EvidentLogService {

    List<StrLogAccess> getListByWorkspaceId(String workspaceId, Long offset, Long limit,
                                            OrderSpecifier<String> orderByColumn) throws EarthException;
}
