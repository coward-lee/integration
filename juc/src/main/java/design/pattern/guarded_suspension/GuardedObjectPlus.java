package design.pattern.guarded_suspension;

import basic.method.InterruptDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Hashtable;
import java.util.Set;

/**
 * @Classname GuardedObjectPlus
 * @Description TODO
 * @Date 2021/6/22 23:43
 * @Create by Lee
 */
public class GuardedObjectPlus extends GuardedObject01 {
	private  int id = 1;

	public GuardedObjectPlus() {
	}

	public GuardedObjectPlus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
class MailBox{
	private static Hashtable<Integer,GuardedObjectPlus> boxes = new Hashtable<>();
	private static  int id = 1;

	// c产生ID
	private static synchronized int generateId(){
		return id++;
	}

	public static GuardedObjectPlus createGuardedObject(){
		int gId = generateId();
		GuardedObjectPlus go = new GuardedObjectPlus(gId);
		boxes.put(gId,go);
		return go;
	}
	public static Set<Integer> getIds(){
		return boxes.keySet();
	}
	public static GuardedObjectPlus getGuardedObjectPlus(int id){
		return boxes.remove(id);
	}


}

class People extends Thread{
	static Logger log = LoggerFactory.getLogger(InterruptDemo.class);

	@Override
	public void run() {
		GuardedObjectPlus go = MailBox.createGuardedObject();
		log.debug("id:{}开始接收信件...",go.getId());
		Object o = go.get(5000);
		log.debug("id:{}已接收信件...",go.getId());
	}
}

class Postman extends Thread{
	static Logger log = LoggerFactory.getLogger(InterruptDemo.class);

	private int mailId ;
	private String letter;

	public Postman(int mailId, String letter) {
		this.mailId = mailId;
		this.letter = letter;
	}

	@Override
	public void run() {

		GuardedObjectPlus go = MailBox.getGuardedObjectPlus(mailId);
		log.debug("id:{} 开始发信...",go.getId());
		go.complete(letter);
		log.debug("id:{} 已发信...",go.getId());

	}
}

