package pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.net.Socket;

/**
 * Created by xubai on 2018/11/02 6:06 PM.
 */
public class ConnectionPoolFactory {

    private GenericObjectPool<Socket> connectionPool;

    public ConnectionPoolFactory(String host, int port, GenericObjectPoolConfig config) {
        PooledObjectFactory<Socket> connectionFactory = new ConnectionFactory(host, port);
        connectionPool = new GenericObjectPool<Socket>(connectionFactory, config);
    }

    public Socket getConnection()throws Exception{
        return connectionPool.borrowObject();
    }

    public void releaseConnection(Socket connection)throws Exception{
        connectionPool.returnObject(connection);

    }

    public Status getPoolStatus(){
        Status st = new Status();
        st.setFactoryType(connectionPool.getFactoryType());
        st.setMaxIdle(connectionPool.getMaxIdle());
        st.setActiveNum(connectionPool.getNumActive());
        st.setIdleNum(connectionPool.getNumIdle());
        st.setWaitersNum(connectionPool.getNumWaiters());
        st.setBorrowedCount((int)connectionPool.getBorrowedCount());
        st.setCreatedCount((int)connectionPool.getCreatedCount());
        st.setDestroyedCount((int)connectionPool.getDestroyedCount());
        st.setReturnedCount((int)connectionPool.getReturnedCount());
        return st;
    }


    class Status{
        private String factoryType;
        private int maxIdle;
        private int activeNum;
        private int idleNum;
        private int waitersNum;
        private int createdCount;
        private int borrowedCount;
        private int destroyedCount;
        private int returnedCount;

        public Status() {
        }

        public Status(String factoryType, int maxIdle, int activeNum, int idleNum, int waitersNum, int createdCount, int borrowedCount, int destroyedCount, int returnedCount) {
            this.factoryType = factoryType;
            this.maxIdle = maxIdle;
            this.activeNum = activeNum;
            this.idleNum = idleNum;
            this.waitersNum = waitersNum;
            this.createdCount = createdCount;
            this.borrowedCount = borrowedCount;
            this.destroyedCount = destroyedCount;
            this.returnedCount = returnedCount;
        }

        public void setFactoryType(String factoryType) {
            this.factoryType = factoryType;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public void setActiveNum(int activeNum) {
            this.activeNum = activeNum;
        }

        public void setIdleNum(int idleNum) {
            this.idleNum = idleNum;
        }

        public void setWaitersNum(int waitersNum) {
            this.waitersNum = waitersNum;
        }

        public void setCreatedCount(int createdCount) {
            this.createdCount = createdCount;
        }

        public void setBorrowedCount(int borrowedCount) {
            this.borrowedCount = borrowedCount;
        }

        public void setDestroyedCount(int destroyedCount) {
            this.destroyedCount = destroyedCount;
        }

        public void setReturnedCount(int returnedCount) {
            this.returnedCount = returnedCount;
        }

        @Override
        public String toString() {
            return "Status{" +
                    "factoryType='" + factoryType + '\'' +
                    ", maxIdle=" + maxIdle +
                    ", activeNum=" + activeNum +
                    ", idleNum=" + idleNum +
                    ", waitersNum=" + waitersNum +
                    ", createdCount=" + createdCount +
                    ", borrowedCount=" + borrowedCount +
                    ", destroyedCount=" + destroyedCount +
                    ", returnedCount=" + returnedCount +
                    '}';
        }
    }

}
