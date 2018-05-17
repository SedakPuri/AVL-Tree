import java.util.Iterator;

public interface Set<E>  
{
	public boolean add(E k);
	public boolean remove(E k);
	public boolean contains(E k);
	public Iterator<E> iterator();
	
	public void addAll(Set<E> other);
	public void retainAll(Set<E> other);
	public void removeAll(Set<E> other);

}
