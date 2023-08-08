package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.HandlerEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.DataMatrix;
import com.lxkplus.RandomInit.model.RandomParam;
import com.lxkplus.RandomInit.model.RandomParamList;
import com.lxkplus.RandomInit.service.RandomService;
import com.lxkplus.RandomInit.utils.ExcelWriter;
import com.lxkplus.RandomInit.utils.ListUtils;
import com.lxkplus.RandomInit.utils.RandomDataMaker;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RandomServiceImpl implements RandomService {

    @Resource
    ExcelWriter excelWriter;

    @Resource
    RandomDataMaker randomDataMaker;

    @Override
    public DataMatrix fillRandomData(RandomParamList rowParams) throws NormalErrorException {
        DataMatrix dataMatrix = new DataMatrix();
        for (RandomParam randomParam : rowParams.getParamsList()) {
            if (randomParam.getCount() == null) {
                throw new NormalErrorException(ErrorEnum.NotEnoughParams, "缺乏数量信息");
            }
            if (randomParam.getBuilderRuler() == null) {
                throw new NormalErrorException(ErrorEnum.NotEnoughParams, "缺乏生成规则");
            }
            if (randomParam.getColumnName() == null) {
                throw new NormalErrorException(ErrorEnum.NotEnoughParams, "缺乏字段名");
            }

            HandlerEnum handlerEnum = HandlerEnum.valueOf(randomParam.getBuilderRuler());
            dataMatrix.addColumn(randomParam.getColumnName(), createRandomList(handlerEnum, randomParam));
        }
        return dataMatrix;
    }

    @Override
    public List<String> sqlBuilder(String tableName, DataMatrix dataMatrix) {
        List<String> stringList = new ArrayList<>();

        Set<String> rowNameList = dataMatrix.getDataColumnList().keySet();

        String rows = StringUtils.join(rowNameList, ",");
        Collection<List<String>> values = dataMatrix.getDataColumnList().values();
        values = ListUtils.rowNormaliser(new ArrayList<>(values));
        for (List<String> value : values) {
            List<String> collect =
                    value.stream()
                            .map(StringEscapeUtils::escapeHtml4)
                            .map(x -> "'" + x + "'")
                            .collect(Collectors.toList());
            String join = StringUtils.join(collect, ",");

            String format = String.format("INSERT INTO TABLE %s(%s) values( %s );", tableName, rows, join);

            stringList.add(format);
        }
        return stringList;
    }

    @Override
    public void DataMatrixToExcel(DataMatrix dataMatrix, Path path) throws IOException {
        List<List<String>> objects = new ArrayList<>();
        for (Map.Entry<String, List<String>> stringListEntry : dataMatrix.getDataColumnList().entrySet()) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(stringListEntry.getKey());
            temp.addAll(stringListEntry.getValue());
            objects.add(temp);
        }

        objects = ListUtils.rowNormaliser(objects);
        excelWriter.excelMaker(objects, path);
    }


    List<String> createRandomList(HandlerEnum handlerEnum, RandomParam randomParam) throws NormalErrorException {
        switch (handlerEnum) {
            case ip:
                return randomDataMaker.getIP(randomParam.getCount());
            case filePath:
                return randomDataMaker.getFilePath(randomParam.getCount());
            case passWord:
                return randomDataMaker.getPassword(randomParam.getCount());
            case uuid:
                return randomDataMaker.randomUUID(randomParam.getCount());
            case bool:
                return randomDataMaker.getBoolean(randomParam.getCount());
            case personName:
                return randomDataMaker.getPersonName(randomParam.getCount());
            case city:
                return randomDataMaker.getCity(randomParam.getCount());
            case moneyNumber:
                return randomDataMaker.getMoney(randomParam.getCount());
            case phoneNumber:
                return randomDataMaker.getPhoneNumber(randomParam.getCount());
            case email:
                return randomDataMaker.getEmail(randomParam.getCount());
            case regex:
                if (randomParam.getRegex() == null) {
                    throw new NormalErrorException(ErrorEnum.NotEnoughParams, "缺乏正则表达式参数");
                }
                return randomDataMaker.randomByPattern(randomParam.getCount(), randomParam.getRegex());
            case creditID:
                return randomDataMaker.getCreditID(randomParam.getCount());
            case streetAddress:
                return randomDataMaker.getStreetAddress(randomParam.getCount());
            case increaseID:
                Integer startIndex = Objects.requireNonNullElse(randomParam.getStartIndex(), 0);
                return randomDataMaker.getIncreaseID(randomParam.getCount(), startIndex);
            case randomTime:
                Date startTime = Objects.requireNonNullElse(randomParam.getStartTime(), DateUtils.addYears(new Date(), -5));
                Date endTime = Objects.requireNonNullElse(randomParam.getEndTime(), DateUtils.addYears(new Date(), 5));
                Boolean sorted = Objects.requireNonNullElse(randomParam.isSorted(), false);
                return randomDataMaker.getRandomTime(randomParam.getCount(), startTime, endTime, sorted);
            case randomInteger:
                Integer min = Objects.requireNonNullElse(randomParam.getMin(), Integer.MIN_VALUE);
                Integer max = Objects.requireNonNullElse(randomParam.getMax(), Integer.MAX_VALUE);
                sorted = Objects.requireNonNullElse(randomParam.isSorted(), false);
                return randomDataMaker.randomInteger(randomParam.getCount(), min, max, sorted);
            case university:
                return randomDataMaker.getUniversity(randomParam.getCount());
            case NULL:
                ArrayList<String> objects = new ArrayList<>(randomParam.getCount());
                for (int i = 0; i < randomParam.getCount(); i++) {
                    objects.add("null");
                }
                return objects;
            default:
                throw new NormalErrorException(ErrorEnum.NotHaveRuler);
        }
    }
}
