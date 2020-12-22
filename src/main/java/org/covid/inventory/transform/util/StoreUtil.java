package org.covid.inventory.transform.util;

import org.covid.inventory.entity.Store;
import org.covid.inventory.entity.StoreTimings;
import org.covid.inventory.model.StoreDto;
import org.covid.inventory.model.StoreTimeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class StoreUtil {

    @Autowired
    private ModelMapper modelMapper;

    public StoreDto toDto(Store store) {
    	return modelMapper.map(store, StoreDto.class);
//        StoreDto storeDto = new StoreDto();
//        Set<StoreTimeDto> timeDtos = new HashSet<>();
//        storeDto.setStoreTimings(timeDtos);
//        for (StoreTimings storeTimings : store.getStoreTimings()) {
//            storeDto.getStoreTimings().add(storeTimeDto(storeTimings));
//        }
//
//        storeDto.setId(store.getId());
//        storeDto.setDeliveryInSlot(store.getDeliveryInSlot());
//        storeDto.setStoreName(store.getStoreName());
//        return storeDto;
    }

    private StoreTimeDto storeTimeDto(StoreTimings storeTimings) {
        return modelMapper.map(storeTimings, StoreTimeDto.class);
    }


    public Store toEntity(StoreDto storeDto) {
    	Store store = modelMapper.map(storeDto, Store.class);
    	store.setActive(true);
    	return store;
//        Store store = new Store();
//        Set<StoreTimings> timingsSet = new HashSet<>();
//        store.setStoreTimings(timingsSet);
//        store.setStoreName(storeDto.getStoreName());
//        store.setDeliveryInSlot(storeDto.getDeliveryInSlot());
//        store.setSlotDuration(storeDto.getSlotDuration());
//
//        for (StoreTimeDto dto : storeDto.getStoreTimings()) {
//           store.getStoreTimings().add(mapStore(dto));
//        }
//        return store;
    }

    private StoreTimings mapStore(StoreTimeDto timeDto) {
        return modelMapper.map(timeDto, StoreTimings.class);
    }
}
