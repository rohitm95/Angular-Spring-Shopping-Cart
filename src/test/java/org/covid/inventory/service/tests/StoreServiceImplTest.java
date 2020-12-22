package org.covid.inventory.service.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.covid.inventory.service.impl.StoreServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import org.covid.inventory.datajpa.StoreRepository;
import org.covid.inventory.entity.Store;

@RunWith(MockitoJUnitRunner.class)
class StoreServiceImplTest {

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private EntityManager entityManager;

	@InjectMocks
	private StoreServiceImpl storeServiceImpl;
	Store store;

	Store res = null;

	@Test
	void getStoreByName_TestName() {
		MockitoAnnotations.initMocks(this);
		store = MockData.getStore(1);
		when(storeRepository.getStoreByName(Mockito.anyString())).thenReturn(store);
		res = storeServiceImpl.getStoreByName(Mockito.anyString());
		assertEquals(res.getStoreName(), "Kamal");
	}

	@Test
	void getStoreByName_TestId() {
		MockitoAnnotations.initMocks(this);
		store = MockData.getStore(1);
		when(storeRepository.getStoreByName(Mockito.anyString())).thenReturn(store);
		res = storeServiceImpl.getStoreByName(Mockito.anyString());
		assertEquals(res.getId(), 1);

	}

	@Test
	void getStoreByName_TestCreatedBy() {
		MockitoAnnotations.initMocks(this);
		store = MockData.getStore(1);
		when(storeRepository.getStoreByName(Mockito.anyString())).thenReturn(store);
		res = storeServiceImpl.getStoreByName(Mockito.anyString());
		assertEquals(res.getCreatedBy(), "admin");

	}

	@Test
	void getStoreByName_TestModifiedBy() {
		MockitoAnnotations.initMocks(this);
		store = MockData.getStore(1);
		when(storeRepository.getStoreByName(Mockito.anyString())).thenReturn(store);
		res = storeServiceImpl.getStoreByName(Mockito.anyString());
		assertNull(res.getModifiedBy());
	}

	@Test
	void getStoreByName_TestSlotDuration() {
		MockitoAnnotations.initMocks(this);
		store = MockData.getStore(1);
		when(storeRepository.getStoreByName(Mockito.anyString())).thenReturn(store);
		res = storeServiceImpl.getStoreByName(Mockito.anyString());
		assertEquals(res.getSlotDuration(), 30);
	}

	@Test
	void getStoreByName_TestDeliveryInSlot() {
		MockitoAnnotations.initMocks(this);
		store = MockData.getStore(1);
		when(storeRepository.getStoreByName(Mockito.anyString())).thenReturn(store);
		res = storeServiceImpl.getStoreByName(Mockito.anyString());
		assertEquals(res.getDeliveryInSlot(), 1);
	}

//	@Test
//	void getStoreById() {
//		MockitoAnnotations.initMocks(this);
//		store = MockData.getStore(1);
//
//		when(storeRepository.findById(1).orElse(null)).thenReturn(store);
//		res = storeServiceImpl.findById(Mockito.anyInt());
//		assertEquals(res.getDeliveryInSlot(),1);
//	}
	@Test
	void findAllStores_ListSizeCheck() {
		MockitoAnnotations.initMocks(this);
		List<Store> storeList = new ArrayList<Store>();

		storeList.add(MockData.getStore(1));
		storeList.add(MockData.getStore(2));
		storeList.add(MockData.getStore(3));
		storeList.add(MockData.getStore(4));
		when(storeRepository.findAll()).thenReturn(storeList);
		List<Store> resList = storeServiceImpl.findAll();
		assertEquals(resList.size(), 4);
	}

//	@Test
//	void findAllStores_CheckingStoreId() {
//		MockitoAnnotations.initMocks(this);
//		List<Store> storeList = new ArrayList<Store>();
//
//		storeList.add(MockData.getStore(1));
//		storeList.add(MockData.getStore(2));
//		storeList.add(MockData.getStore(3));
//		storeList.add(MockData.getStore(4));
//		when(storeRepository.findAll()).thenReturn(storeList);
//		List<Store> resList = storeServiceImpl.findAll();
//		assertEquals(resList.get(0).getId(), 1);
//	}
	@Test
	void findAllStores_CheckingStoreName() {
		MockitoAnnotations.initMocks(this);
		List<Store> storeList = new ArrayList<Store>();

		storeList.add(MockData.getStore(1));
		storeList.add(MockData.getStore(2));
		storeList.add(MockData.getStore(3));
		storeList.add(MockData.getStore(4));
		when(storeRepository.findAll()).thenReturn(storeList);
		List<Store> resList = storeServiceImpl.findAll();
		assertEquals(resList.get(0).getStoreName(), "Kamal");
	}

	@Test
	void findAllStores_NotNull() {
		MockitoAnnotations.initMocks(this);
		List<Store> storeList = new ArrayList<Store>();

		storeList.add(MockData.getStore(1));
		storeList.add(MockData.getStore(2));
		storeList.add(MockData.getStore(3));
		storeList.add(MockData.getStore(4));
		when(storeRepository.findAll()).thenReturn(storeList);
		List<Store> resList = storeServiceImpl.findAll();
		assertNotNull(resList);
	}

	@Test
	void findAllStores_Null() {
		MockitoAnnotations.initMocks(this);
		List<Store> storeList = null;
		when(storeRepository.findAll()).thenReturn(storeList);
		List<Store> resList = storeServiceImpl.findAll();
		assertNull(resList);
	}
	@Test
	void updateStore() {
		MockitoAnnotations.initMocks(this);
		store = MockData.getStore(2);
		when(entityManager.merge(store)).thenReturn(store);
		when(entityManager.find(Store.class, store.getId())).thenReturn(store);

		res = storeServiceImpl.update(store);
		assertEquals(res.getId(),2);
	}
	@Test
	void updateStore_notNull() {
		MockitoAnnotations.initMocks(this);
		store = MockData.getStore(2);
		when(entityManager.merge(store)).thenReturn(store);
		when(entityManager.find(Store.class, store.getId())).thenReturn(store);

		res = storeServiceImpl.update(store);
		assertNotNull(res);
	}
	@Test
	void saveStore() {
		MockitoAnnotations.initMocks(this);
		store = MockData.getStore(2);
		
		when(storeRepository.save(store)).thenReturn(store);

		res = storeServiceImpl.save(store);
		assertNotNull(res);
	}
}
