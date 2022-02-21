package design.parttern.strategy;

/**
 * spring mvc : HandlerExecutionChain
 */
public class StrategyMain {
    public static void main(String[] args) {
        WildDuck wildDuck = new WildDuck();
        wildDuck.fly();
        ToyDuck toyDuck = new ToyDuck();
        toyDuck.fly();
        PekingDuck pekingDuck = new PekingDuck();
        pekingDuck.fly();

/*
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
     HandlerExecutionChain mappedHandler = null;
        mappedHandler = getHandler(processedRequest);
    // applyPreHandle 再这个方法的内部得到了handlerInterceptor
    // 再pre方法中还调用了：triggerAfterCompletion 方法，而该方法调用了如下面的方法
    if (!mappedHandler.applyPreHandle(processedRequest, response)) {
        return;
     }
     applyDefaultViewName(processedRequest, mv);
     //  拦截器的 interceptor.postHandler()
     mappedHandler.applyPostHandle(processedRequest, response, mv);
 }

void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, @Nullable Exception ex) {
    for (int i = this.interceptorIndex; i >= 0; i--) {
        HandlerInterceptor interceptor = this.interceptorList.get(i);
        try {
            interceptor.afterCompletion(request, response, this.handler, ex);
        }
        catch (Throwable ex2) {
            logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
        }
    }
}
     */


    }
}
