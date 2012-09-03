package org.infinispan.distribution.ch;

import org.infinispan.dataplacement.lookup.ObjectLookup;
import org.infinispan.remoting.transport.Address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The consistent hash function implementation that the Object Lookup implementations from the Data Placement 
 * optimization
 *
 * @author Zhongmiao Li
 * @author João Paiva
 * @author Pedro Ruivo
 * @since 5.2
 */
public class DataPlacementConsistentHash extends AbstractConsistentHash {

   private ConsistentHash defaultConsistentHash;
   private final Map<Address, ObjectLookup> lookUpperList;
   private final List<Address> addressList;

   public DataPlacementConsistentHash(List<Address> addressList) {
      this.addressList = new ArrayList<Address>(addressList);
      lookUpperList = new HashMap<Address, ObjectLookup>();
   }

   public void addObjectLookup(Address address, ObjectLookup objectLookup) {
      if (objectLookup == null) {
         return;
      }
      lookUpperList.put(address, objectLookup);
   }

   @Override
   public void setCaches(Set<Address> caches) {
      defaultConsistentHash.setCaches(caches);
   }

   @Override
   public Set<Address> getCaches() {
      return defaultConsistentHash.getCaches();
   }

   @Override
   public List<Address> locate(Object key, int replCount) {
      List<Address> defaultAddList = defaultConsistentHash.locate(key, replCount);
      ObjectLookup objectLookup = lookUpperList.get(defaultAddList.get(0));
      int index = objectLookup == null ? ObjectLookup.KEY_NOT_FOUND : objectLookup.query(key);

      if (index == ObjectLookup.KEY_NOT_FOUND) {
         return defaultAddList;
      } else {
         List<Address> addList = new ArrayList<Address>();
         addList.add(addressList.get(index));
         return addList;
      }
   }

   @Override
   public List<Integer> getHashIds(Address a) {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   public void setDefault(ConsistentHash defaultHash) {
      defaultConsistentHash = defaultHash;
   }

   public ConsistentHash getDefaultHash() {
      return defaultConsistentHash;
   }
}