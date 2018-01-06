package com.foodfinder.day;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.day.domain.entity.TimedDish;
import com.foodfinder.day.domain.mapper.DayMapper;
import com.foodfinder.day.repository.DayRepository;
import com.foodfinder.day.service.DayService;
import com.foodfinder.day.service.HitsService;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class DayServiceTest {

    @MockBean
    private DayRepository dayRepository;

    @MockBean
    private DayMapper dayMapper;

    @MockBean
    private HitsService hitsService;

    private DayService dayService;
    private PageRequest defaultPageRequest;
    private Day day;
    private DayDTO dayDTO;
    private List<Day> days;
    private List<DayDTO> daysDTO;
    private Page<Day> daysPage;

    @Before
    public void setup() {
        dayService = new DayService(dayRepository, dayMapper, hitsService);
        defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        day = Day.builder()
                .id(1L)
                .name("Monday")
                .date(new Date(0))
                .timedDishes(new HashSet<>(Collections.singletonList(new TimedDish())))
                .build();
        days = Collections.singletonList(day);
        daysPage = new PageImpl<>(days);

        dayDTO = DayDTO.builder()
                .id(1L)
                .name("Monday")
                .date(new Date(0))
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();
        daysDTO = Collections.singletonList(dayDTO);

        ReflectionTestUtils.setField(dayService, "dateFormat", "1970-01-01");
    }

    @Test
    public void givenDayId_whenGetDay_thenReturnDto() throws Exception {
        given(dayRepository.findOne(1L)).willReturn(day);
        given(dayMapper.toDto(day)).willReturn(dayDTO);

        assertEquals(dayService.getDay(1L), dayDTO);
    }

    @Test
    public void whenGetDays_thenReturnDtoList() throws Exception {
        given(dayRepository.findAll(defaultPageRequest)).willReturn(daysPage);
        given(dayMapper.dayListToDto(daysPage)).willReturn(daysDTO);

        assertEquals(dayService.getDayList(defaultPageRequest, null, null, null), daysDTO);
    }

    @Test
    public void givenDate_whenGetDays_thenReturnDtoList() throws Exception {
        given(dayRepository.findByDateString("1970-01-01")).willReturn(days);
        given(dayMapper.dayListToDto(days)).willReturn(daysDTO);

        assertEquals(dayService.getDayList(defaultPageRequest, new Date(0), null, null), daysDTO);
    }

    @Test
    public void givenDateFromAndTo_whenGetDays_thenReturnDtoList() throws Exception {
        given(dayRepository.findByDateBetween(new Date(0), new Date(1000))).willReturn(days);
        given(dayMapper.dayListToDto(days)).willReturn(daysDTO);

        assertEquals(dayService.getDayList(defaultPageRequest, null, new Date(0), new Date(1000)), daysDTO);
    }
}
