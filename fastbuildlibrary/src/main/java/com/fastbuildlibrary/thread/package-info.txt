/*
###########################################
FBL 异步执行代码块

###########################################
FBLTaskExecuteQueue 按照队列的形式一个一个处理任务，一个线程处理一个FBLTaskExecuteQueue

使用说明：
(1)新建任务队列
FBLTaskExecuteQueue tq = new FBLTaskExecuteQueue();
(2)执行任务
tq.execute(new FBLTaskItem() {

            @Override
            public List<Object> execute() {
                return null;
            }

            @Override
            public void finished(List<Object> result) {

            }
});

###########################################
TaskThreadPool
使用说明：
(1)获取线程池管理实例
FBLTaskThreadPool tp = FBLTaskThreadPool.getInstance();
(2)执行任务
tp.execute(new FBLTaskItem() {
            @Override
            public List<Object> execute() {
                return null;
            }

            @Override
            public void finished(List<Object> result) {

            }
});

###########################################
注意：
TaskItem 为抽象类，必须实现里面的抽象方法，抽象方法execute里面的代码是在新线程中执行的，不能直接执行UI操作。
*/