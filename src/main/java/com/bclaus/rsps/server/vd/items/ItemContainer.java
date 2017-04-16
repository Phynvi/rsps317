package com.bclaus.rsps.server.vd.items;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

import com.bclaus.rsps.server.vd.player.Player;

/**
 * A collection of items that can be manipulated through the functions contained
 * in this, or the {@link Collections} class. Other classes may also extend this
 * class to inherit, and/or build on its functions.
 * 
 * @author lare96
 */
public class ItemContainer extends AbstractCollection<Item> {

	/** The maximum amount of items that can be put into this container. */
	private final int capacity;

	/** The array of items in this container. */
	private Item[] items;

	/** The policy of this container */
	private final Policy policy;

	/**
	 * A set of constants that define how items will be stacked in this
	 * collection.
	 * 
	 * @author lare96
	 */
	public enum Policy {
		NORMAL, STACK_ALWAYS, STACK_NEVER
	}

	/**
	 * Create a new {@link ItemContainer}.
	 * 
	 * @param policy
	 *            the policy of this container.
	 * @param capacity
	 *            the initial capacity of this container.
	 */
	public ItemContainer(Policy policy, int capacity) {
		this.policy = policy;
		this.capacity = capacity;
		this.items = new Item[capacity];
	}

	/**
	 * Create a new {@link ItemContainer} with the argued collection.
	 * 
	 * @param policy
	 *            the policy of this container.
	 * @param collection
	 *            the collection to back this container with.
	 */
	public ItemContainer(Policy policy, Collection<Item> collection) {
		this(policy, collection.size());
		super.addAll(collection);
	}

	/**
	 * Adds an item to the argued slot in this container.
	 * 
	 * @param item
	 *            the item to add to this container.
	 * @param slot
	 *            the preferred slot to add this item in, -1 will add the item
	 *            in any free slot.
	 * @return <code>true</code> if the container was modified as a result of
	 *         the call, <code>false</code> otherwise.
	 */
	public boolean add(Item item, int slot) {
		if (item == null || item.getId() < 1 || item.getCount() < 1 || item.getCount() > Integer.MAX_VALUE) {
			return false;
		}
		boolean stackable = Item.itemStackable[item.getId()];
		int newSlot = (slot > -1) ? slot : getFreeSlot();
		if ((stackable || policy.equals(Policy.STACK_ALWAYS)) && !policy.equals(Policy.STACK_NEVER)) {
			if (totalAmount(item.getId()) > 0) {
				newSlot = getSlot(item.getId());
			}
		}
		if (newSlot == -1) {
			return false;
		}
		if (get(newSlot) != null) {
			newSlot = getFreeSlot();
		}

		if (stackable || policy == Policy.STACK_ALWAYS && policy != Policy.STACK_NEVER) {
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null && items[i].getId() == item.getId()) {
					set(i, new Item(items[i].getId(), items[i].getCount() + item.getCount()));
					return true;
				}
			}
			if (newSlot == -1) {
				return false;
			}
			set(slot > -1 ? newSlot : getFreeSlot(), item);
			return true;
		}

		int remainingSlots = getRemainingSlots();
		if (item.getCount() > remainingSlots && !stackable) {
			item.setCount(remainingSlots);
		}

		for (int i = 0; i < item.getCount(); i++) {
			set(slot > -1 ? newSlot : getFreeSlot(), new Item(item.getId(), 1));
		}
		return true;
	}

	/**
	 * Removes the argued item from the argued slot in this container.
	 * 
	 * @param item
	 *            the item to remove from this container.
	 * @param slot
	 *            the preferred slot to remove this item from, -1 will remove
	 *            the item from the first slot the item is found in.
	 * @return <code>true</code> if the container was modified as a result of
	 *         the call, <code>false</code> otherwise.
	 */
	public boolean remove(Item item, int slot) {
		if (item == null || item.getId() < 1 || item.getCount() < 1 || item.getCount() > Integer.MAX_VALUE) {
			return false;
		}
		boolean stackable = Item.itemStackable[item.getId()];
		if ((stackable || policy.equals(Policy.STACK_ALWAYS)) && !policy.equals(Policy.STACK_NEVER)) {
			int slotHolder = getSlot(item.getId());
			Item stack = get(slotHolder);
			if (stack == null) {
				return false;
			}
			if (stack.getCount() > item.getCount()) {
				set(slotHolder, new Item(stack.getId(), stack.getCount() - item.getCount()));
			} else {
				set(slotHolder, null);
			}
		} else {
			for (int i = 0; i < item.getCount(); i++) {
				int slotHolder = getSlot(item.getId());
				if (i == 0 && slot != -1) {
					Item inSlot = get(slot);
					if (inSlot == null) {
						return false;
					}
					if (inSlot.getId() == item.getId()) {
						slotHolder = slot;
					}
				}
				if (slotHolder != -1) {
					set(slotHolder, null);
				} else {
					break;
				}
			}
		}
		return true;
	}

	/**
	 * Removes the argued item from this container.
	 * 
	 * @param item
	 *            the item to remove from this container.
	 * @return <code>true</code> if the container was modified as a result of
	 *         the call, <code>false</code> otherwise.
	 */
	public boolean remove(Item item) {
		return remove(item, -1);
	}

	/**
	 * Transfers an existing item in the argued slot to the argued new slot. If
	 * an item is present in the argued new slot, all of the items in this
	 * container will be shifted to accommodate for the transfer.
	 * 
	 * @param slot
	 *            the slot the existing item is in.
	 * @param newSlot
	 *            the new slot to move the existing item to.
	 */
	public void transfer(int slot, int newSlot) {
		Item from = items[slot];
		if (from == null) {
			return;
		}
		items[slot] = null;
		if (slot > newSlot) {
			int shiftFrom = newSlot;
			int shiftTo = slot;
			for (int i = (newSlot + 1); i < slot; i++) {
				if (items[i] == null) {
					shiftTo = i;
					break;
				}
			}
			Item[] slice = new Item[shiftTo - shiftFrom];
			System.arraycopy(items, shiftFrom, slice, 0, slice.length);
			System.arraycopy(slice, 0, items, shiftFrom + 1, slice.length);
		} else {
			int sliceStart = slot + 1;
			int sliceEnd = newSlot;
			for (int i = (sliceEnd - 1); i >= sliceStart; i--) {
				if (items[i] == null) {
					sliceStart = i;
					break;
				}
			}
			Item[] slice = new Item[sliceEnd - sliceStart + 1];
			System.arraycopy(items, sliceStart, slice, 0, slice.length);
			System.arraycopy(slice, 0, items, sliceStart - 1, slice.length);
		}
		items[newSlot] = from;
	}

	/**
	 * Determines if there is enough space in this container to add the argued
	 * item.
	 * 
	 * @param item
	 *            the item to determine if there is enough space in this
	 *            container for.
	 * @return <code>true</code> if there is enough space to add the item,
	 *         <code>false</code> otherwise.
	 */
	public boolean spaceFor(Item item) {
		boolean stackable = Item.itemStackable[item.getId()];
		if (stackable || policy == Policy.STACK_ALWAYS && policy != Policy.STACK_NEVER) {
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null && items[i].getId() == item.getId()) {
					int totalCount = item.getCount() + items[i].getCount();
					if (totalCount >= Integer.MAX_VALUE || totalCount < 1) {
						return false;
					}
					return true;
				}
			}
			int slot = getFreeSlot();
			return slot != -1;
		}

		int slots = getRemainingSlots();
		return slots >= item.getCount();
	}

	/**
	 * Determines if this container has any items with the argued ID.
	 * 
	 * @param id
	 *            the ID to check this container for.
	 * @return <code>true</code> if this container has at least one item with
	 *         the argued ID, <code>false</code> otherwise.
	 */
	public boolean contains(int id) {
		return getSlot(id) != -1;
	}

	/**
	 * Determines if this container has any items with all of the argued ID's.
	 * 
	 * @param ids
	 *            the ID's to check this container for.
	 * @return <code>true</code> if this container has at least one item with
	 *         all of the argued ID's, <code>false</code> otherwise.
	 */
	public boolean containsAll(int... ids) {
		return Arrays.stream(ids).allMatch(id -> contains(id));
	}

	/**
	 * Determines if this container has any items with any of the argued ID's.
	 * 
	 * @param ids
	 *            the ID's to check this container for.
	 * @return <code>true</code> if this container has at least one item with
	 *         any of the argued ID's, <code>false</code> otherwise.
	 */
	public boolean containsAny(int... ids) {
		return Arrays.stream(ids).anyMatch(id -> contains(id));
	}

	/**
	 * Determines if this container has any items with all of the argued ID's.
	 * 
	 * @param ids
	 *            the ID's to check this container for.
	 * @return <code>true</code> if this container has at least one item with
	 *         all of the argued ID's, <code>false</code> otherwise.
	 */
	public boolean containsAll(Item... items) {
		return Arrays.stream(items).filter(Objects::nonNull).allMatch(item -> contains(item));
	}

	/**
	 * Determines if this container has any items with all of the argued ID's.
	 * 
	 * @param ids
	 *            the ID's to check this container for.
	 * @return <code>true</code> if this container has at least one item with
	 *         all of the argued ID's, <code>false</code> otherwise.
	 */
	public boolean containsAny(Item... items) {
		return Arrays.stream(items).filter(Objects::nonNull).anyMatch(item -> contains(item));
	}

	/***
	 * Determines if the argued slot does not have an item.
	 * 
	 * @param slot
	 *            the slot to determine is free or not.
	 * @return <code>true</code> if the argued slot has no item,
	 *         <code>false</code> otherwise.
	 */
	public boolean isSlotFree(int slot) {
		return items[slot] == null;
	}

	/***
	 * Determines if the argued slot has an item.
	 * 
	 * @param slot
	 *            the slot to determine is used or not.
	 * @return <code>true</code> if the argued slot has an item,
	 *         <code>false</code> otherwise.
	 */
	public boolean isSlotUsed(int slot) {
		return !isSlotFree(slot);
	}

	/**
	 * Places the argued item on the argued slot. This method does not take into
	 * account the existing item on the argued slot.
	 * 
	 * @param slot
	 *            the slot to place the item in.
	 * @param item
	 *            the item to place.
	 */
	public void set(int slot, Item item) {
		items[slot] = item;
	}

	/**
	 * Sets the backing array of items to the argued array of items. <b>The
	 * backing array will not hold any references to the argued array when this
	 * method completes.</b>
	 * 
	 * @param items
	 *            the new array of items to use as the backing array, the length
	 *            of the array must be equal to the capacity of this container.
	 */
	public void setItems(Item[] items) {
		clear();
		for (int i = 0; i < items.length; i++) {
			this.items[i] = items[i] == null ? null : items[i].clone();
		}
	}

	/**
	 * Shifts all items in the backing array to the left to fill any gaps with
	 * <code>null</code> elements.
	 */
	public void shift() {
		Item[] previousItems = items;
		items = new Item[capacity];
		int newIndex = 0;
		for (int i = 0; i < items.length; i++) {
			if (previousItems[i] != null) {
				items[newIndex] = previousItems[i];
				newIndex++;
			}
		}
	}

	/**
	 * Swaps the position of two different items.
	 * 
	 * @param slot
	 *            the slot that will be switched.
	 * @param switchSlot
	 *            the other slot that will be switched.
	 */
	public void swap(int slot, int switchSlot) {
		Item temp = get(slot);
		set(slot, get(switchSlot));
		set(switchSlot, temp);
	}

	/**
	 * Refreshes the contents of this container to the argued widget.
	 * 
	 * @param widget
	 *            the widget to refresh the contents of this container on.
	 */
	public void refresh(int widget, Player player) {
		// player.getItems().
		// player.getPacketBuilder().sendUpdateItems(widget, toArray());
	}

	/**
	 * Gets the first item found in this container with the argued item ID.
	 * 
	 * @param itemId
	 *            the item ID to retrieve an item in this container with.
	 * @return the first item found in this container with the argued item ID,
	 *         or <code>null</code> if no item was found.
	 */
	public Optional<Item> getItem(int itemId) {
		return Arrays.stream(items).filter(item -> item != null && itemId == item.getId()).findFirst();
	}

	/**
	 * Gets the item ID of the item on the argued slot.
	 * 
	 * @param slot
	 *            the slot to get the item ID from.
	 * @return the item ID of the item on this slot, or will throw a
	 *         {@link NullPointerException} if no items are on this slot.
	 */
	public int getItemId(int slot) {
		if (items[slot] == null)
			return -1;

		return items[slot].getId();
	}

	/**
	 * Gets the item on the argued slot.
	 * 
	 * @param slot
	 *            the slot to get the item from.
	 * @return the item on the argued slot, or <code>null</code> if no item
	 *         exists on this slot.
	 */
	public Item get(int slot) {
		if (slot == -1 || slot >= items.length)
			return null;
		return items[slot];
	}

	/**
	 * Gets the slot of the first item found with the argued item ID.
	 * 
	 * @param itemId
	 *            the item ID of the item to get the slot from.
	 * @return the slot of the first item found, or -1 if it was not found.
	 */
	public int getSlot(int itemId) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null || items[i].getId() != itemId)
				continue;
			return i;
		}
		return -1;
	}

	/**
	 * Gets the total amount of items with the argued item ID.
	 * 
	 * @param itemId
	 *            the item ID to get the total amount of.
	 * @return the total amount of items with the argued item ID.
	 */
	public int totalAmount(int itemId) {
		return Arrays.stream(items).filter(item -> item != null && item.getId() == itemId).mapToInt(item -> item.getCount()).sum();
	}

	/**
	 * Gets an empty slot from this container.
	 * 
	 * @return the empty slot, or -1 if this container is full.
	 */
	public int getFreeSlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the amount of free remaining slots in this container.
	 * 
	 * @return the amount of free remaining slots in this container
	 */
	public int getRemainingSlots() {
		return capacity - size();
	}

	/**
	 * Gets the capacity of the backing array.
	 * 
	 * @return the capacity of the backing array.
	 */
	public int capacity() {
		return capacity;
	}

	@Override
	public boolean add(Item item) {
		return add(item, -1);
	}

	/**
	 * Performs a check on all items in this container to see if they match the
	 * argued Object. Rather than using <code>equals</code> to check if the
	 * elements match, it checks whether the Object is an item and if so
	 * compares the ID and quantity. This method will throw a
	 * {@link NullPointerException} if the argued Object has a value of
	 * <code>null</code>.
	 */
	@Override
	public boolean contains(Object o) {
		if (!(o instanceof Item))
			return false;
		Item item = (Item) o;
		return Arrays.stream(items).filter(Objects::nonNull).anyMatch(i -> i.getId() == item.getId() && i.getCount() >= item.getCount());
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof Item))
			return false;
		return remove((Item) o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Objects.requireNonNull(c);
		boolean modified = false;
		for (Object o : c)
			if (remove(o))
				modified = true;
		return modified;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		Objects.requireNonNull(c);
		return removeIf(i -> !c.stream().anyMatch(item -> ((Item) item).getId() == i.getId() && ((Item) item).getCount() >= i.getCount()));
	}

	@Override
	public void clear() {
		items = new Item[capacity];
	}

	@Override
	public int size() {
		return Arrays.stream(items).filter(Objects::nonNull).mapToInt(item -> 1).sum();
	}

	@Override
	public Item[] toArray() {
		return items.clone();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("This operation is not supported, 'container()' should be used instead.");
	}

	@Override
	public Iterator<Item> iterator() {
		return new Iterator<Item>() {

			/** The current index we are iterating over. */
			private int currentIndex;

			/** The last index we iterated over. */
			private int lastElementIndex = -1;

			@Override
			public boolean hasNext() {
				return !(currentIndex + 1 > items.length);
			}

			@Override
			public Item next() {
				if (currentIndex >= items.length) {
					throw new ArrayIndexOutOfBoundsException("Nothing left to iterate over!");
				}

				int i = currentIndex;
				currentIndex++;
				return items[lastElementIndex = i];
			}

			@Override
			public void remove() {
				if (lastElementIndex < 0) {
					throw new IllegalStateException("Can only call 'remove()' once in call to 'next()'.");
				}

				ItemContainer.this.remove(items[lastElementIndex], lastElementIndex);
				currentIndex = lastElementIndex;
				lastElementIndex = -1;
			}
		};
	}

	@Override
	public ItemContainer clone() {
		ItemContainer c = new ItemContainer(policy, capacity);
		c.items = items.clone();
		return c;
	}
}