package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.model.Client;
import ru.otus.service.ClientService;

/**
 * @author Shabunina Anita
 */
@Controller
@RequiredArgsConstructor
public class ClientController {

    private static final String CLIENTS = "clients";
    private final ClientService clientService;

    @GetMapping("/clients")
    public ModelAndView getAllClients(ModelAndView mav) {
        mav.getModel().put(CLIENTS, clientService.getAll());
        mav.setViewName(CLIENTS);
        return mav;
    }

    @PostMapping("/clients")
    public String addClient(@ModelAttribute Client client) {
        clientService.add(client);
        return "redirect:/clients";
    }
}
