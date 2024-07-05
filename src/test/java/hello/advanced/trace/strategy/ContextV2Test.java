package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.cod.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
/*
*
* */
@Slf4j
public class ContextV2Test {

    /**
    * 전략 패턴 적용
    **/
    @Test
    void strategyV1(){
        //context를 실행하는 시점에 원하는 Strategy를 전달할 수 있다.
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }

    /**
     * 전략 패턴 익명 내부 클래스
     **/
    @Test
    void strategyV2(){
        //context를 실행하는 시점에 원하는 Strategy를 전달할 수 있다.
        ContextV2 context = new ContextV2();
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }

    /**
     * 전략 패턴 람다
     **/
    @Test
    void strategyV3(){
        //context를 실행하는 시점에 원하는 Strategy를 전달할 수 있다.
        ContextV2 context = new ContextV2();
        context.execute(() -> log.info("비즈니스 로직1 실행"));
        context.execute(() -> log.info("비즈니스 로직2 실행"));
    }
}

