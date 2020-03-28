package murskiy_sergey.graduate_work.controllers.weka;

import murskiy_sergey.graduate_work.models.weka.data.WekaDataModel;
import murskiy_sergey.graduate_work.services.weka.WekaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/weka/model")
public class WekaDataController {
    private final WekaDataService wekaDataService;

    @Autowired
    public WekaDataController(WekaDataService wekaDataService) {
        this.wekaDataService = wekaDataService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<WekaDataModel> getAllWekaDataModels() {
        return wekaDataService.getAllWekaModels();
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public WekaDataModel getWekaModelByName(@PathVariable("name") String name) {
        return wekaDataService.getWekaModelByModelName(name);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public WekaDataModel getActiveWekaModel() {
        return wekaDataService.getActiveDataModel();
    }

    @RequestMapping(value = "/build/random", method = RequestMethod.POST)
    public ResponseEntity<String> buildWekaDataModel(@RequestParam("modelName") String modelName,
                                             @RequestParam("sizeOfAttributes") int sizeOfAttributes,
                                             @RequestParam("setActive") boolean setActive) {
        return new ResponseEntity<>(wekaDataService.buildWekaDataModel(modelName, sizeOfAttributes, setActive), HttpStatus.OK);
    }

    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public String changeWekaDataModel(@RequestParam("modelName") String modelName) {
        return wekaDataService.changeWekaDataModel(modelName);
    }

    @RequestMapping(value = "/names", method = RequestMethod.GET)
    public List<String> getModelsNames() {
        return wekaDataService.getModelsNames();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteModel(@RequestParam("modelName") String modelName) {
        wekaDataService.deleteModel(modelName);
    }

    @RequestMapping(value = "/delete/all")
    public void deleteAllModels() {
        wekaDataService.deleteAllWekaDataModels();
    }
}
