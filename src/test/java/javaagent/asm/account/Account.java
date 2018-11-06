package javaagent.asm.account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xubai on 2018/10/17 下午3:34.
 */
public class Account {

    private Map<Integer, Double> accountBook = new ConcurrentHashMap<Integer, Double>(){
        {
            put(1, 99.8d);
            put(2, 05.6d);
            put(3, 34.1d);
        }
    };

    public boolean operate(int accountId, String password, double money){
        double userAccount = accountBook.getOrDefault(accountId, 0.0d);
        if (userAccount > money){
            synchronized (this){
                double balance = userAccount - money;
                accountBook.put(accountId, balance);
                System.out.println("accountId:"+accountId+" account:"+userAccount+" operate money:"+money+" balance:"+balance);
                return true;
            }
        }
        return false;
    }

}
