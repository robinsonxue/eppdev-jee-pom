/* FileName: _EppdevVersionDao.java
 * Copyright EPPDEV-JEE, All Rights Preserved!
 * License: Apache License 2.0
 * Auto created by eppdev-jee(http://jee.eppdev.cn)!
 */

package cn.eppdev.jee.conf.dao.auto;

import cn.eppdev.jee.commons.dao.BasicDao;
import cn.eppdev.jee.conf.entity.EppdevVersion;
import cn.eppdev.jee.conf.param.EppdevVersionParam;
import org.apache.ibatis.annotations.Param;

/**
 * _eppdev_version对应的基础Dao，请勿修改，代码生成时会自动进行覆盖
 * @author fan.hao
 */
public interface _EppdevVersionDao extends BasicDao<EppdevVersion, EppdevVersionParam> {
    public int realDelete(@Param("id") String id);

}
