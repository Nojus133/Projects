using Microsoft.AspNetCore.Mvc;
using oblig1.DAL;
using oblig1.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace oblig1.Controllers
{
    [Route("[controller]/[action]")]
    public class BestillingController : ControllerBase
    {
        private readonly IBussReiseRepository _db;

        public BestillingController(IBussReiseRepository db)
        {
            _db = db;
        }

        public async Task<List<Strekninger>> HentSteder()
        {
            return await _db.HentSteder();
        }

        public async Task<List<Rute>> FinnBillett (Bestilling b)
        {

            return await _db.FinnBillett(b);
        }

        public async Task<List<BillettType>> GetPris()
        {
            return await _db.GetPris();
        }

        public async Task<int> LagreBillettene(Bestilling[] innBilletter)
        {
           return await _db.LagreBillettene(innBilletter);
        }

        public async Task<List<EnkeltBillett>> HentBilletter(int id)
        {
            return await _db.HentBilletter(id);
        }
    }
}
