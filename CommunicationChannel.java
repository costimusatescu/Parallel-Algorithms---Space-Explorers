/**
 * Class that implements the channel used by headquarters and space explorers to communicate.
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CommunicationChannel {

	ArrayBlockingQueue<Message> channelSpaceExplorer = new ArrayBlockingQueue<>(500);
	ArrayBlockingQueue<Message> channelHeadQuarter = new ArrayBlockingQueue<>(500);
	ConcurrentHashMap<Long, Message> hashMap = new ConcurrentHashMap<Long, Message>();


	/**
	 * Creates a {@code CommunicationChannel} object.
	 */
	public CommunicationChannel() {
	}

	/**
	 * Puts a message on the space explorer channel (i.e., where space explorers write to and 
	 * headquarters read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageSpaceExplorerChannel(Message message) {
		try {
			channelSpaceExplorer.put(message);
		} catch (InterruptedException e) {
			
		}
	}

	/**
	 * Gets a message from the space explorer channel (i.e., where space explorers write to and
	 * headquarters read from).
	 * 
	 * @return message from the space explorer channel
	 */
	public Message getMessageSpaceExplorerChannel() {
		Message message = null;
		try { message = channelSpaceExplorer.take();
		} catch (InterruptedException e) {
			
		}
		return message;
	}

	/**
	 * Puts a message on the headquarters channel (i.e., where headquarters write to and 
	 * space explorers read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageHeadQuarterChannel(Message message) {
		long id = Thread.currentThread().getId();
		if (message.getData().equals("EXIT")) {

			try {
				channelHeadQuarter.put(new Message(-1, "EXIT"));
			} catch (InterruptedException e) {
				
			}
		} else if (message.getData().equals("END")) {
		}
		else if (hashMap.containsKey(id)) {
			
			try {
				channelHeadQuarter.put(new Message(hashMap.get(id).getCurrentSolarSystem(),
						message.getCurrentSolarSystem(), message.getData()));
			} catch (InterruptedException e) {
				
			}
			hashMap.remove(id);
		} else {
			hashMap.put(id, message);
		}
	}

	/**
	 * Gets a message from the headquarters channel (i.e., where headquarters write to and
	 * space explorer read from).
	 * 
	 * @return message from the header quarter channel
	 */
	public Message getMessageHeadQuarterChannel() {
		Message message = null;
		try { message = channelHeadQuarter.take();
		} catch (InterruptedException e) {
			
		}
		return message;
	}
}
