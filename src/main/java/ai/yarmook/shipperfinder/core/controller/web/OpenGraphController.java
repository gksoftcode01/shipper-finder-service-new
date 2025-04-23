package ai.yarmook.shipperfinder.core.controller.web;

import ai.yarmook.shipperfinder.service.TripQueryService;
import ai.yarmook.shipperfinder.service.criteria.TripCriteria;
import ai.yarmook.shipperfinder.service.dto.TripDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import tech.jhipster.service.filter.UUIDFilter;

@Controller
public class OpenGraphController {

    private final TripQueryService tripQueryService;

    public OpenGraphController(TripQueryService tripQueryService) {
        this.tripQueryService = tripQueryService;
    }

    @GetMapping("/og/trip/{encId}")
    public ModelAndView openGraph(@PathVariable String encId) {
        ModelAndView modelAndView = new ModelAndView("web/og");
        TripCriteria tripCriteria = new TripCriteria();
        UUIDFilter uuidFilter = new UUIDFilter();
        uuidFilter.setEquals(UUID.fromString(encId));
        tripCriteria.setEncId(uuidFilter);
        List<TripDTO> list = tripQueryService.findByCriteria(tripCriteria, PageRequest.of(0, 1)).toList();
        if (!list.isEmpty()) {
            TripDTO trip = list.get(0);
            modelAndView.addObject("title", "Trip - Shipper Finder");
            modelAndView.addObject("ogTitle", "Trip " + trip.getFromCountry().getName() + " to " + trip.getToCountry().getName());
            modelAndView.addObject("ogUrl", "http://localhost:8080/trip/" + encId);
            modelAndView.addObject(
                "ogImage",
                "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA1s8lXc.img?w=768&h=432&m=6&x=898&y=235&s=401&d=401"
            );
            modelAndView.addObject("ogDescription", "We will fetch all other to be shown");
        } else {}
        return modelAndView;
    }
}
