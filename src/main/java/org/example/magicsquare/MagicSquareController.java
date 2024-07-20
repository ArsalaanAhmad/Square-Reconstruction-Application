package org.example.magicsquare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({"magicSquare", "size"})
public class MagicSquareController {
/*
    @Autowired
    private LeaderboardService leaderboardService;
*/
    @GetMapping("/")
    public String splash() {
        return "splash";
    }

    @GetMapping("/enterName")
    public String index() {
        return "index";
    }

    @PostMapping("/enterName")
    public String enterName(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "enterSize";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam("name") String name, @RequestParam("size") int size, Model model) {
        if (size % 2 == 0 || size <= 1) {
            model.addAttribute("error", "Size must be an odd number greater than 1.");
            model.addAttribute("name", name);
            return "enterSize";
        }

        int[][] magicSquare = MagicSquareGenerator.generateMagicSquare(size);
        MagicSquareGenerator.shuffleMagicSquare(magicSquare);
        model.addAttribute("magicSquare", magicSquare);
        model.addAttribute("size", size);
        model.addAttribute("name", name);
        return "result";
    }

    @PostMapping("/swap")
    public String swap(@RequestParam("name") String name,
                       @RequestParam("x1") int x1, @RequestParam("y1") int y1,
                       @RequestParam("x2") int x2, @RequestParam("y2") int y2,
                       Model model, @SessionAttribute("magicSquare") int[][] magicSquare,
                       @SessionAttribute("size") int size) {

        // Perform the swap
        MagicSquareGenerator.swap(magicSquare, x1, y1, x2, y2);

        model.addAttribute("magicSquare", magicSquare);
        model.addAttribute("size", size);
        model.addAttribute("name", name);
        return "result";
    }

    @PostMapping("/validate")
    public String validate(@SessionAttribute("magicSquare") int[][] magicSquare, Model model) {
        boolean isValid = MagicSquareGenerator.isValidMagicSquare(magicSquare);
        int[] sums = MagicSquareGenerator.getSums(magicSquare);
        model.addAttribute("isValid", isValid);
        model.addAttribute("sums", sums);
        return "validationResult";
    }
    }

    /*

    @PostMapping("/saveLeaderboard")
    public String saveLeaderboard(@RequestParam("name") String name,
                                  @RequestParam("moves") int moves,
                                  @RequestParam("size") int size,
                                  Model model) {
        double score = (double) moves / size;
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setPlayerName(name);
        leaderboard.setMoves(moves);
        leaderboard.setSize(size);
        leaderboard.setScore(score);
        leaderboardService.save(leaderboard);

        return "redirect:/leaderboard";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        List<Leaderboard> leaderboardList = leaderboardService.findAll();
        model.addAttribute("leaderboard", leaderboardList);
        return "leaderboard";
    }
}

     */