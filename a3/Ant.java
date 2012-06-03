package a3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Implementierung des Ameiseninterfaces.
 */
class Ant implements IAnt
{
    private List<Integer> path;
    private final int capacity;
    private int load;
    private final Set<Integer> customers;
    private Set<Integer> visitedCustomers;
    private boolean isGoingOut = true;


    /**
     * Konstuktor einer Ameise mit Übergabe der Startecke und Kapazität.
     *
     * @param capacity Kapazität einer Ameise
     * @param start	Index einer Ecke
     */
    Ant(int start, int capacity, Set<Integer> customers)
    {
        path = new ArrayList<Integer>();
        path.add(start);

        this.capacity = capacity;
        this.load = capacity;
        this.customers = customers;
        this.visitedCustomers = new HashSet<Integer>();
    }

    @Override
    public int getLoad()
    {
        return this.load;
    }
    
    @Override
    public void addVisitedCustomer(int customer)
    {
        this.visitedCustomers.add(customer);
    }

    @Override
    public Set<Integer> getRemainingCustomers()
    {
        Set<Integer> remaining = new HashSet<Integer>(this.customers);
        remaining.removeAll(this.visitedCustomers);
        return remaining;
    }
    
    @Override
    public boolean isAtCustomer()
    {
        return this.getRemainingCustomers().contains(this.currentPosition());
    }

    @Override
    public void decreaseLoad(int decreaseValue)
    {
        this.load -= decreaseValue;
    }

    @Override
    public void refill()
    {
        this.load = this.capacity;
    }

    @Override
    public List<Integer> getPath()
    {
        return new ArrayList<Integer>(path);
    }

    @Override
    public void moveTo(int vertex)
    {
        path.add(vertex);
    }

    @Override
    public int currentPosition()
    {
        return path.get(path.size() - 1);
    }

    @Override
    public boolean isGoingHome()
    {
        return !this.isGoingOut;
    }
    
    @Override
    public void setGoingHome()
    {
        this.isGoingOut = false;
    }

    @Override
    public boolean isGoingOut()
    {
        return this.isGoingOut;
    }

    @Override
    public void setGoingOut()
    {
        this.isGoingOut = true;
    }

    @Override
    public void reset()
    {
        int temp = path.get(0);
        path.clear();
        path.add(temp);

        this.refill();
        this.visitedCustomers.clear();
        this.setGoingOut();
    }

    @Override
    public String toString()
    {
        return String.format("Ant: %s => %s", this, this.getPath());
    }
}
