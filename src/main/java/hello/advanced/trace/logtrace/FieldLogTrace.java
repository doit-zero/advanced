package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldLogTrace implements LogTrace{
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";
    private TraceId traceIdHolder; // ㅌ레이스 Id 동기화 동시성 문제 발생
    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder;
        Long startTimeMs = System.currentTimeMillis();
        //로그 출력
        log.info("[{}] {}{}",traceId.getId(),addSpace(START_PREFIX,traceId.getLevel()),message);
        return new TraceStatus(traceId,startTimeMs,message);
    }

    private void syncTraceId(){
        if(traceIdHolder == null){
            traceIdHolder = new TraceId();
        } else {
            traceIdHolder = traceIdHolder.createNexId();
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status,null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status,e);
    }

    private void complete(TraceStatus status,Exception e){
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[" + traceId.getId() + "] " + addSpace(COMPLETE_PREFIX,
                    traceId.getLevel()) + status.getMessage() + " time=" + resultTimeMs + "ms");
        } else {
            log.info("[" + traceId.getId() + "] " + addSpace(EX_PREFIX,
                    traceId.getLevel()) + status.getMessage() + " time="
                    + resultTimeMs + "ms" + " ex=" + e);
        }
        
        releaseTraceIf();
    }

    private void releaseTraceIf() {
        if(traceIdHolder.isFirstLevel()){
            traceIdHolder = null;
        } else {
            traceIdHolder = traceIdHolder.createPreviousId();
        }
    }

    private Object addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < level; i++){
            sb.append((i==level-1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }
}
