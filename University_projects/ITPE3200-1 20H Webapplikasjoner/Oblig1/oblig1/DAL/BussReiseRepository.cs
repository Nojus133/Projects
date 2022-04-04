using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using oblig1.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;

namespace oblig1.DAL
{
    public class BussReiseRepository : IBussReiseRepository
    {
        private readonly BussReiseContext _db;

        public BussReiseRepository(BussReiseContext db)
        {
            _db = db;
        }

        public async Task<List<Strekninger>> HentSteder()
        {
            try
            {
                List<Strekninger> alleSteder = await _db.Strekninger.Select(s => new Strekninger
                {
                    StedNavn = s.StedNavn
                }).ToListAsync();
                return alleSteder;
            }
            catch
            {
                return null;
            }
        }

        public async Task<List<Rute>> FinnBillett(Bestilling b)
        {
            try
            {
                List<Rute> funnetRutene = await _db.Ruter.Where(r => r.StedNavn.StedNavn == b.Fra && r.TId.Ukedag == b.getUkedag()).ToListAsync();
                return funnetRutene;
            }
            catch
            {
                return null;
            }
        }

        public async Task<int> LagreBillettene(Bestilling[] innBilletter)
        {
            var nyBestilling = new Bestillingen
            {
                BestillingsDato = innBilletter[0].BestillingsDato,
                TotaltPris = innBilletter[0].TotalPris
            };
            
            var sjekkKunde = await _db.Kunder.SingleOrDefaultAsync(k => k.Epost == innBilletter[0].Epost);

            if (sjekkKunde == null)
            {
                var enKunde = new Kunder
                {
                    Epost = innBilletter[0].Epost,
                    Telefon = innBilletter[0].Telefon
                };
                nyBestilling.Kunder = enKunde;
            }
            else
            {
                 if (sjekkKunde.Telefon != innBilletter[0].Telefon)
                 {
                    sjekkKunde.Telefon = innBilletter[0].Telefon;
                    await _db.SaveChangesAsync();
                 }
                 nyBestilling.Kunder = sjekkKunde;


            }

            foreach (Bestilling b in innBilletter)
            {
                BillettType enBillettType = await _db.BillettTyper.SingleOrDefaultAsync(e => e.Type == b.Reisende);
                Strekninger reiseFra = await _db.Strekninger.SingleOrDefaultAsync(e => e.StedNavn == b.Fra);
                Strekninger reiseTil = await _db.Strekninger.SingleOrDefaultAsync(e => e.StedNavn == b.Til);
                Rute ruten = await _db.Ruter.FindAsync(b.Rute);

                var nyBillett = new EnkeltBillett
                {
                    ReiseDato = b.ReiseDato,
                    Bestillinger = nyBestilling,
                    BillettTyper = enBillettType,
                    Fra = reiseFra,
                    Til = reiseTil,
                    Ruten = ruten
                };
                _db.EnkeltBilletter.Add(nyBillett);
            }
            await _db.SaveChangesAsync();

            var id = nyBestilling.BId;
            return id;
        }

       
        public async Task<List<BillettType>> GetPris()
        {
            List<BillettType> prisene = await _db.BillettTyper.ToListAsync();
            return prisene;

        }

        public async Task<List<EnkeltBillett>> HentBilletter(int id)
        {
            List<EnkeltBillett> funnetBillettene = await _db.EnkeltBilletter.Where(b => b.Bestillinger.BId == id).ToListAsync();
            return funnetBillettene;
        }
    }
}
