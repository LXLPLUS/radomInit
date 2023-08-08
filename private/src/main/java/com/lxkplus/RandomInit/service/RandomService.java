package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.DataMatrix;
import com.lxkplus.RandomInit.model.RandomParamList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface RandomService {
    DataMatrix fillRandomData(RandomParamList rowParams) throws NormalErrorException;
    List<String> sqlBuilder(String tableName, DataMatrix dataMatrix);

    void DataMatrixToExcel(DataMatrix dataMatrix, Path path) throws NormalErrorException, IOException;

}
