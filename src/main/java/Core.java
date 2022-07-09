public class Core implements Runnable
{
    Processor processor;
    int id;

    public Core(Processor processor, int id)
    {
        this.processor = processor;
        this.id = id;
    }

    public void run()
    {
        try
        {
            Thread.sleep(10000);
        }
        catch(InterruptedException iex)
        {
            iex.printStackTrace();
        }
        while(true)
        {
            processor.cpuCoreAllocator(id);
        }
    }
}