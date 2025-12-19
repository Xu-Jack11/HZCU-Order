package com.hzcu.order.service;

import com.hzcu.order.entity.Canteen;
import com.hzcu.order.repository.CanteenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CanteenServiceTest {

    @Mock
    private CanteenRepository canteenRepository;

    @InjectMocks
    private CanteenService canteenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getActiveCanteens_returnsOnlyActive() {
        Canteen c1 = Canteen.builder().name("Canteen 1").status(1).build();
        Canteen c2 = Canteen.builder().name("Canteen 2").status(1).build();
        when(canteenRepository.findByStatusOrderBySortOrderAsc(1)).thenReturn(Arrays.asList(c1, c2));

        List<Canteen> active = canteenService.getActiveCanteens();

        assertEquals(2, active.size());
        verify(canteenRepository, times(1)).findByStatusOrderBySortOrderAsc(1);
    }

    @Test
    void getCanteenById_returnsCanteen() {
        Canteen canteen = Canteen.builder().canteenId(1L).name("Test Canteen").build();
        when(canteenRepository.findById(1L)).thenReturn(Optional.of(canteen));

        Optional<Canteen> result = canteenService.getCanteenById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Canteen", result.get().getName());
    }

    @Test
    void saveCanteen_callsRepository() {
        Canteen canteen = Canteen.builder().name("New Canteen").build();
        when(canteenRepository.save(any(Canteen.class))).thenReturn(canteen);

        Canteen saved = canteenService.saveCanteen(canteen);

        assertNotNull(saved);
        assertEquals("New Canteen", saved.getName());
        verify(canteenRepository).save(any(Canteen.class));
    }
}
