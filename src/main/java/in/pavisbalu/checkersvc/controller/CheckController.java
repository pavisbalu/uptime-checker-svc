package in.pavisbalu.checkersvc.controller;

import in.pavisbalu.checkersvc.model.Check;
import in.pavisbalu.checkersvc.model.CheckRun;
import in.pavisbalu.checkersvc.service.CheckService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/check")
public class CheckController {
    @Autowired
    private CheckService checkService;

    @PostMapping
    public void create(@Valid @RequestBody Check check) {
        checkService.save(check);
    }

    // TODO: Would be nice to have pagination in here when we integrate it with the UI
    @GetMapping
    public Iterable<Check> list(
            @RequestParam(value = "name", required = false) String nameSubstring,
            @RequestParam(value = "interval", required = false) String interval) {
        if (StringUtils.isNotBlank(nameSubstring)) {
            return checkService.findChecksByName(nameSubstring);
        } else if (StringUtils.isNotBlank(interval)) {
            return checkService.findChecksByInterval(Duration.parse(interval));
        } else {
            return checkService.all();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long checkId) {
        checkService.delete(checkId);
    }

    @GetMapping("/{id}/history")
    public List<CheckRun> history(@PathVariable("id") Long checkId) {
        return checkService.history(checkId);
    }

    @PostMapping("/{id}/activate")
    public void activateCheck(@PathVariable("id") Long checkId) {
        checkService.activateCheck(checkId);
    }

    @PostMapping("/{id}/deactivate")
    public void deactivateCheck(@PathVariable("id") Long checkId) {
        checkService.deactivateCheck(checkId);
    }

    // Useful to debug what's in queue for the next scheduler run
    @GetMapping("/next")
    public List<Check> nextToRun() {
        return checkService.findChecksToExecute();
    }

    // Useful during development to see why certain requests fail
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
