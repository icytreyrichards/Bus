package network;

public interface Tracker {
public void pre_execute();
public void onDone(String result);
public void onError();

}
