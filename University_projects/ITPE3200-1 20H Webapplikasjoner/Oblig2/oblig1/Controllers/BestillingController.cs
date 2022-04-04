using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using oblig1.DAL;
using oblig1.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;

namespace oblig1.Controllers
{
    [Route("[controller]/[action]")]
    public class BestillingController : ControllerBase
    {
        private readonly IBussReiseRepository _db;

        private ILogger<BestillingController> _log;

        private const string _loggetInn = "loggetInn";
        public BestillingController(IBussReiseRepository db, ILogger<BestillingController> log)
        {
            _db = db;
            _log = log;
        }

        public async Task<ActionResult> HentSteder()
        {
            List<Strekninger> alleStrekninger = await _db.HentSteder();
            return Ok(alleStrekninger);
        }

        public async Task<ActionResult> FinnBillett (Bestilling b)
        {
            List<Rute> funnetRutene = await _db.FinnBillett(b);
            if (funnetRutene.Count == 0)
            {
                _log.LogInformation("Fant ikke rutene");
                return NotFound("Fant ikke rutene");
            }
            return Ok(funnetRutene);
        }

        public async Task<ActionResult> GetPris()
        {
            List<BillettType> funnetBilletter = await _db.GetPris();
            if (funnetBilletter.Count == 0)
            {
                _log.LogInformation("Kunne ikke hente billett priser");
                return NotFound("Kunne ikke hente billett priser");
            }
            return Ok(funnetBilletter);
        }

        public async Task<ActionResult> LagreBillettene(Bestilling[] innBilletter)
        {
            int? funnetBestilling = await _db.LagreBillettene(innBilletter);
            return Ok(funnetBestilling);
        }

        public async Task<ActionResult> HentBilletter(int id)
        {
            List<EnkeltBillett> funnetBillettene = await _db.HentBilletter(id);
            return Ok(funnetBillettene);
        }

        public async Task<ActionResult> LoggInn(Bruker bruker)
        {
            if (ModelState.IsValid)
            {
                bool ok = await _db.LoggInn(bruker);
                if (!ok)
                {
                    _log.LogInformation("Innloggingsfeil");
                    HttpContext.Session.SetString(_loggetInn, "");
                    return Ok(false);
                }
                HttpContext.Session.SetString(_loggetInn, "LoggetInn");
                return Ok(true);
            }
            _log.LogInformation("Feil i inputvalidering");
            return BadRequest("Input validerings feil på server");
        }

        public void LoggUt()
        {
            HttpContext.Session.SetString(_loggetInn, "");
        }

        public ActionResult ValiderBruker()
        {
            if (string.IsNullOrEmpty(HttpContext.Session.GetString(_loggetInn)))
            {
                _log.LogInformation("Uautorisert bruker");
                return Unauthorized();
            }
            return Ok();
        }



        public async Task<ActionResult> HentStrekninger()
        {
            List<Strekninger> alleStrekninger = await _db.HentStrekninger();
            if (alleStrekninger != null)
            {
                return Ok(alleStrekninger);
            }
            _log.LogInformation("Kunne ikke hente strekninger");
            return NotFound("Kunne ikke hente strekninger");
        }

        public async Task<ActionResult> HentKunder()
        {
            List<Kunder> alleKunder = await _db.HentKunder();
            if (alleKunder != null)
            {
                return Ok(alleKunder);
            }
            _log.LogInformation("Kunne ikke hente kunder");
            return NotFound("Kunne ikke hente kunder");
        }

        public async Task<ActionResult> HentBestillinger()
        {
            List<Bestillingen> alleBestillinger = await _db.HentBestillinger();
            if (alleBestillinger != null)
            {
                return Ok(alleBestillinger);
            }
            _log.LogInformation("Kunne ikke hente bestillinger");
            return NotFound("Kunne ikke hente bestillinger");
        }

        public async Task<ActionResult> HentAlleBilletter()
        {
            List<EnkeltBillett> funnetBillettene = await _db.HentAlleBilletter();
            if (funnetBillettene != null)
            {
                return Ok(funnetBillettene);
            }
            _log.LogInformation("Kunne ikke hente alle billettene");
            return NotFound("Kunne ikke hente alle billettene");
        }

        public async Task<ActionResult> HentRuter()
        {
            List<Rute> alleRuter = await _db.HentRuter();
            if (alleRuter != null)
            {
                return Ok(alleRuter);
            }
            _log.LogInformation("Kunne ikke hente alle rutene");
            return NotFound("Kunne ikke hente alle rutene");
        }

        public async Task<ActionResult> HentRutene(string id)
        {
            List<Rute> funnetRutene = await _db.HentRutene(id);
            if (funnetRutene != null)
            {
                return Ok(funnetRutene);
            }
            _log.LogInformation("Kunne ikke hente rutene");
            return NotFound("Kunne ikke hente rutene");
        }

        public async Task<ActionResult> HentTidsplaner()
        {
            List<Tidsplaner> alleTidsplaner = await _db.HentTidsplaner();
            if (alleTidsplaner != null)
            {
                return Ok(alleTidsplaner);
            }
            _log.LogInformation("Kunne ikke hente tidsplaner");
            return NotFound("Kunne ikke hente tidsplaner");
        }

        
        public async Task<ActionResult> LagreType(BillettType b)
        {
            if (ModelState.IsValid)
            {
                bool ok = await _db.LagreType(b);
                if (!ok)
                {
                    _log.LogInformation("Verdiene er ikke valide");
                    return BadRequest("Verdiene er ikke valide");
                }
                return Ok("Billett type lagret");
            }
            _log.LogInformation("Verdiene er ikke valide");
            return BadRequest("Verdiene er ikke valide");
        }

        public async Task<ActionResult> LagreStrekning(Strekninger s)
        {
            if (ModelState.IsValid)
            {
                bool ok = await _db.LagreStrekning(s);
                if (!ok)
                {
                    _log.LogInformation("Verdiene er ikke valide");
                    return BadRequest("Verdiene er ikke valide");
                }
                return Ok("Ny Strekning lagret");
            }
            _log.LogInformation("Verdiene er ikke valide");
            return BadRequest("Verdiene er ikke valide");
        }

        public async Task<ActionResult> LagreTidsplan(Tidsplaner t)
        {
            if (ModelState.IsValid)
            {
                bool ok = await _db.LagreTidsplan(t);
                if (!ok)
                {
                    _log.LogInformation("Verdiene er ikke valide");
                    return BadRequest("Verdiene er ikke valide");
                }
                return Ok("Ny tidsplan lagret");
            }
            _log.LogInformation("Verdiene er ikke valide");
            return BadRequest("Verdiene er ikke valide");
        }

        public async Task<ActionResult> LagreRute(int id_sted, int id_tid)
        {
            if (ModelState.IsValid)
            {
                bool ok = await _db.LagreRute(id_sted, id_tid);
                if (!ok)
                {
                    _log.LogInformation("Verdiene er ikke valide");
                    return BadRequest("Verdiene er ikke valide");
                }
                return Ok("Ny Rute lagret");
            }
            _log.LogInformation("Verdiene er ikke valide");
            return BadRequest("Verdiene er ikke valide");
        }


        public async Task<ActionResult> SlettType(int id)
        {
            
            bool ok = await _db.SlettType(id);
            if (ok)
            {
                _log.LogInformation("Billett type slettet");
                return Ok("Billett type slettet");
            }
            _log.LogInformation("Kunne ikke slette raden fra databasen");
            return NotFound("Kunne ikke slette raden fra databasen");
        }

        public async Task<ActionResult> SlettStrekning(int id)
        {

            bool ok = await _db.SlettStrekning(id);
            if (ok)
            {
                _log.LogInformation("Strekning slettet");
                return Ok("Strekning slettet");
            }
            _log.LogInformation("Kunne ikke slette raden fra databasen");
            return NotFound("Kunne ikke slette raden fra databasen");
        }

        public async Task<ActionResult> SlettKunde(int id)
        {

            bool ok = await _db.SlettKunde(id);
            if (ok)
            {
                _log.LogInformation("Kunde slettet");
                return Ok("Kunde slettet");
            }
            _log.LogInformation("Kunne ikke slette raden fra databasen");
            return NotFound("Kunne ikke slette raden fra databasen");
        }

        public async Task<ActionResult> SlettBillett(int id)
        {
            bool ok = await _db.SlettBillett(id);
            if (ok)
            {
                _log.LogInformation("Billett slettet");
                return Ok("Billett slettet");
            }
            _log.LogInformation("Kunne ikke slette raden fra databasen");
            return NotFound("Kunne ikke slette raden fra databasen");
        }

        public async Task<ActionResult> SlettBestilling(int id)
        {

            bool ok = await _db.SlettBestilling(id);
            if (ok)
            {
                _log.LogInformation("Bestilling slettet");
                return Ok("Bestilling slettet");
            }
            _log.LogInformation("Kunne ikke slette raden fra databasen");
            return NotFound("Kunne ikke slette raden fra databasen");
        }

        public async Task<ActionResult> SlettRute(int id)
        {

            bool ok = await _db.SlettRute(id);
            if (ok)
            {
                _log.LogInformation("Rute slettet");
                return Ok("Rute slettet");
            }
            _log.LogInformation("Kunne ikke slette raden fra databasen");
            return NotFound("Kunne ikke slette raden fra databasen");
        }

        public async Task<ActionResult> SlettTidsplan(int id)
        {

            bool ok = await _db.SlettTidsplan(id);
            if (ok)
            {
                _log.LogInformation("Tidsplan slettet");
                return Ok("Tidsplan slettet");
            }
            _log.LogInformation("Kunne ikke slette raden fra databasen");
            return NotFound("Kunne ikke slette raden fra databasen");
        }

        public async Task<ActionResult> EndreType(int id, BillettType obj)
        {
            bool ok = await _db.EndreType(id, obj);
            if (ok)
            {
                _log.LogInformation("Billett type endret");
                return Ok("Billett type endret");
            }
            _log.LogInformation("Kunne ikke oppdatere raden");
            return NotFound("Kunne ikke oppdatere raden");
        }

        public async Task<ActionResult> EndreStrekning(Strekninger obj)
        {
            bool ok = await _db.EndreStrekning(obj);
            if (ok)
            {
                _log.LogInformation("Strekning endret");
                return Ok("Strekning endret");
            }
            _log.LogInformation("Kunne ikke oppdatere raden");
            return NotFound("Kunne ikke oppdatere raden");
        }

        public async Task<ActionResult> EndreKunde(Kunder obj)
        {
            bool ok = await _db.EndreKunde(obj);
            if (ok)
            {
                _log.LogInformation("Kunde endret");
                return Ok("Kunde endret");
            }
            _log.LogInformation("Kunne ikke oppdatere raden");
            return NotFound("Kunne ikke oppdatere raden");
        }

        public async Task<ActionResult> EndreTidsplan(Tidsplaner obj)
        {
            bool ok = await _db.EndreTidsplan(obj);
            if (ok)
            {
                _log.LogInformation("Tidsplan endret");
                return Ok("Tidsplan endret");
            }
            _log.LogInformation("Kunne ikke oppdatere raden");
            return NotFound("Kunne ikke oppdatere raden");
        }

        public async Task<ActionResult> EndreRute(int id, int id_sted, int id_tid)
        {
            bool ok = await _db.EndreRute(id, id_sted, id_tid);
            if (ok)
            {
                _log.LogInformation("Rute endret");
                return Ok("Rute endret");
            }
            _log.LogInformation("Kunne ikke oppdatere raden");
            return NotFound("Kunne ikke oppdatere raden");
        }
    }
}
