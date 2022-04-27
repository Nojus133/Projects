using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using oblig3.DAL;
using oblig3.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace oblig3.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class FaqController : ControllerBase
    {
        private IFaqRepository _db;

        private ILogger<FaqController> _log;

        public FaqController(IFaqRepository db, ILogger<FaqController> log)
        {
            _db = db;
            _log = log;
        }

        [HttpPost]
        public async Task<ActionResult> Lagre(Faq enFaq)
        {
            bool returOK = await _db.Lagre(enFaq);
            if (!returOK)
            {
                _log.LogInformation("Spørsmål kunne ikke lagres");
                return BadRequest();
            }
            return Ok();
        }

        [HttpGet]
        public async Task<ActionResult> HentAlle()
        {
            List<Sporsmaal> alleFaq = await _db.HentAlle();
            return Ok(alleFaq);
        }

        

        [HttpPut]
        public async Task<ActionResult> Endre(Faq enSporsmaal)
        {
                bool returOK = await _db.Endre(enSporsmaal);
                if (!returOK)
                {
                    _log.LogInformation("Endringen kunne ikke utføres");
                    return NotFound();
                }
                return Ok();
        }
    }
}
