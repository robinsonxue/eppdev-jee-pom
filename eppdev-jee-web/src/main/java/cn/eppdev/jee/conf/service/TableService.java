/* FileName: TableService.java
 * Copyright EPPDEV.CN, All Rights Preserved!
 * Licensed By Apache License 2.0
 */

package cn.eppdev.jee.conf.service;

import cn.eppdev.jee.cg.entity.TableFileInfo;
import cn.eppdev.jee.cg.service.ColumnGeneratorService;
import cn.eppdev.jee.cg.service.FileGeneratorService;
import cn.eppdev.jee.cg.utils.FreeMarkerUtils;
import cn.eppdev.jee.cg.utils.GeneratorUtils;
import cn.eppdev.jee.conf.entity.EppdevColumn;
import cn.eppdev.jee.conf.entity.EppdevTable;
import cn.eppdev.jee.share.entity.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fan.hao
 */
@Service
public class TableService {

    @Autowired
    EppdevTableService eppdevTableService;

    @Autowired
    ColumnGeneratorService columnGeneratorService;

    @Autowired
    EppdevColumnService eppdevColumnService;

    @Autowired
    EppdevConfService eppdevConfService;

    @Autowired
    FileGeneratorService fileGeneratorService;

    static Logger logger = LoggerFactory.getLogger(TableService.class);

    public RestResult<EppdevTable> get(String id) {
        try {
            EppdevTable table = eppdevTableService.get(id);
            return new RestResult<>(RestResult.STATUS_SUCCESS, "", table);
        } catch (Exception e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            return new RestResult<>(RestResult.STATUS_FAILED, e.getMessage(), null);
        }
    }

    public RestResult<Integer> update(EppdevTable eppdevTable) {
        try {
            int cnt = eppdevTableService.save(eppdevTable);
            if (cnt == 1) {
                return new RestResult<>(RestResult.STATUS_SUCCESS, "保存成功", cnt);
            } else {
                return new RestResult<>(RestResult.STATUS_FAILED, "保存失败", 0);
            }
        } catch (Exception e) {
            return new RestResult<>(RestResult.STATUS_FAILED, e.getMessage(), 0);
        }
    }


    public RestResult<String> add(EppdevTable eppdevTable) {
        try {
            int cnt = eppdevTableService.insert(eppdevTable);
            if (cnt == 1) {
                List<EppdevColumn> columnList = columnGeneratorService.generateDefaultColumn();
                for (EppdevColumn column: columnList){
                    column.setTableId(eppdevTable.getId());
                    eppdevColumnService.insert(column);
                }
                return new RestResult<>(RestResult.STATUS_SUCCESS, "保存成功", eppdevTable.getId());
            } else {
                return new RestResult<>(RestResult.STATUS_FAILED, "保存失败", null);
            }
        } catch (Exception e) {
            return new RestResult<>(RestResult.STATUS_FAILED, e.getMessage(), null);
        }
    }


    public RestResult<Integer> delete(String id) {
        try {
            int cnt = eppdevTableService.delete(id);
            if (cnt == 1) {
                return new RestResult<>(RestResult.STATUS_SUCCESS, "Success", cnt);
            } else {
                return new RestResult<>(RestResult.STATUS_FAILED, "Table NOT exists?", cnt);
            }
        } catch (Exception e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            return new RestResult<>(RestResult.STATUS_FAILED, e.getMessage(), 0);
        }
    }

    public List<TableFileInfo> getTableFileInfoList(String tableId) {
        List<TableFileInfo> tableFileInfoList = GeneratorUtils.getTableFileInfoList();
        try {
            EppdevTable eppdevTable = get(tableId).getData();
            logger.debug("eppdevTable:{}", eppdevTable);
            List<TableFileInfo> list = new ArrayList<>();
            for (TableFileInfo fileInfo : tableFileInfoList) {
                TableFileInfo tableFileInfo = new TableFileInfo(fileInfo.getType(), fileInfo.getReplace());
                tableFileInfo.setFilePath(FreeMarkerUtils.buildResult(fileInfo.getFilePathTemplate(), eppdevTable));
                list.add(tableFileInfo);
            }
            return list;
        } catch (Exception e){
            e.printStackTrace();
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            return tableFileInfoList;
        }
    }

    public RestResult<String> preview(String tableId, String type){
        try {
            String content = fileGeneratorService.generateContent(tableId, type);
            return new RestResult<>(RestResult.STATUS_SUCCESS, "预览成功", content);
        } catch (Exception e){
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            return new RestResult<>(RestResult.STATUS_FAILED, "预览失败：" + e.getMessage(), null);
        }
    }
}
