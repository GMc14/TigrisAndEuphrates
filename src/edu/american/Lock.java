package edu.american;

//stop the thread from moving forward
public class Lock
{
		//block the thread
        public void lockWait()
        {
                synchronized(this)
                {
                        try{this.wait();}
                        catch(InterruptedException e){}
                }
        }
        //resume the thread
        public void lockResume()
        {
                synchronized(this) {this.notify();}
        }
}

