package pl.cichon.andrzej.springmvcfacelets.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/football")
public class FootballController {
	
	private List<String> players;

	@RequestMapping(value="/{teamname}", method=RequestMethod.GET)
	public ModelAndView foo(@PathVariable String teamname){
		players = new ArrayList<String>();
		ModelMap map = new ModelMap();
		if("barcelona".equalsIgnoreCase(teamname)){
			players.add("Lionel Messi");
			players.add("Andres Iniesta");
			players.add("David Villa");
			players.add("Dani Alves");
			map.addAttribute("team", "FC Barcelona");
		}
		else if("juventus".equalsIgnoreCase(teamname)){
			players.add("Andrea Pirlo");
			players.add("Claudio Marchisio");
			players.add("Arturo Vidal");
			players.add("Andrea Barzagli");
			map.addAttribute("team", "Juventus");
		}
		else if("manutd".equalsIgnoreCase(teamname)){
			players.add("Wayne Rooney");
			players.add("Rio Ferdinand");
			players.add("Robin Van Persie");
			players.add("Shinji Kagawa");
			map.addAttribute("team", "Manchester United");
		}		
		map.addAttribute("players", players);		
		return new ModelAndView("football_players", map);
	}
}
