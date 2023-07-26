package com.example.sqlinit;

import com.example.sqlinit.commons.Timer;
import com.example.sqlinit.exception.NormalErrorException;
import com.example.sqlinit.utils.RandomPatternUtils;
import com.mifmif.common.regex.Generex;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootTest
class SqlInitApplicationTests {

    @Test
   void contextLoads() throws NormalErrorException {
        Timer timer = new Timer();
        timer.flushStartTime();
        System.out.println(RandomPatternUtils.randomByPattern("[0-9]{1,12}", 100));
        timer.flushFinishTime();
        timer.printSpendMillis();
    }

}
