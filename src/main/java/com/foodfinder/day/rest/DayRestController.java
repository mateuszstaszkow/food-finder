package com.foodfinder.day.rest;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.service.DayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/days")
class DayRestController {

    private final DayService dayService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DayDTO> getDayList(Pageable pageable,
                                   @RequestParam(value="date", required = false)
                                       @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                   @RequestParam(value="from", required = false)
                                       @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                   @RequestParam(value="to", required = false)
                                       @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {
        return dayService.getDayList(pageable, date, from, to);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addDay(@RequestBody DayDTO dayDTO) {
        dayService.postDay(dayDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DayDTO getDay(@PathVariable Long id) {
        return dayService.getDay(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateDay(@PathVariable Long id, @RequestBody DayDTO dayDTO) {
        dayService.updateDay(id, dayDTO);
    }
}