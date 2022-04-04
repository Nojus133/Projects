using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using oblig1.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text.Json;
using System.Threading.Tasks;

namespace oblig1.DAL
{
    public class BussReiseRepository : IBussReiseRepository
    {
        private readonly BussReiseContext _db;

        private ILogger<BussReiseRepository> _log;

        public BussReiseRepository(BussReiseContext db, ILogger<BussReiseRepository> log)
        {
            _db = db;
            _log = log;
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
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
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
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }
        }

        public async Task<int?> LagreBillettene(Bestilling[] innBilletter)
        {
            try
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
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }
        }

       
        public async Task<List<BillettType>> GetPris()
        {
            try
            {
                List<BillettType> prisene = await _db.BillettTyper.ToListAsync();
                return prisene;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }

        }

     

        public async Task<List<EnkeltBillett>> HentBilletter(int id)
        {
            try
            {
                List<EnkeltBillett> funnetBillettene = await _db.EnkeltBilletter.Where(b => b.Bestillinger.BId == id).ToListAsync();
                return funnetBillettene;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }
        }

        //TESTE
        public async Task<List<Strekninger>> HentStrekninger()
        {
            try
            {
                List<Strekninger> alleStrekninger = await _db.Strekninger.ToListAsync();
                return alleStrekninger;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }
        }

        public static byte[] LagHash(string passord, byte[] salt)
        {
            return KeyDerivation.Pbkdf2(
                password : passord,
                salt : salt,
                prf : KeyDerivationPrf.HMACSHA512,
                iterationCount : 1000,
                numBytesRequested : 32);
        }

        public static byte[] LagSalt()
        {
            var csp = new RNGCryptoServiceProvider();
            var salt = new byte[24];
            csp.GetBytes(salt);
            return salt;
        }
 
        public async Task<bool> LoggInn(Bruker bruker)
        {
            try
            {
                Brukere funnetBruker = await _db.Brukere.FirstOrDefaultAsync(b => b.Brukernavn == bruker.Brukernavn);
                byte[] hash = LagHash(bruker.Passord, funnetBruker.Salt);
                bool ok = hash.SequenceEqual(funnetBruker.Passord);
                if (ok)
                {
                    return true;
                }
                return false;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }



        public async Task<List<Kunder>> HentKunder()
        {
            try
            {
                List<Kunder> alleKunder = await _db.Kunder.ToListAsync();
                return alleKunder;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }
        }

        public async Task<List<Bestillingen>> HentBestillinger()
        {
            try
            {
                List<Bestillingen> alleBestillinger = await _db.Bestillinger.ToListAsync();
                return alleBestillinger;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }
        }

        public async Task<List<EnkeltBillett>> HentAlleBilletter()
        {
            try
            {
                List<EnkeltBillett> alleBillettene = await _db.EnkeltBilletter.ToListAsync();
                return alleBillettene;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }
        }

        public async Task<List<Rute>> HentRuter()
        {
            try
            {
                List<Rute> alleRutene = await _db.Ruter.ToListAsync();
                return alleRutene;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }
        }

        public async Task<List<Rute>> HentRutene(string id)
        {
            try
            {

                List<Rute> funnetRutene = await _db.Ruter.Where(r => r.StedNavn.StedNavn == id).ToListAsync();
                
                return funnetRutene;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }
        }

        public async Task<List<Tidsplaner>> HentTidsplaner()
        {
            try
            {
                List<Tidsplaner> alletidsplaner = await _db.Tidsplaner.ToListAsync();
                return alletidsplaner;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return null;
            }
        }

        public async Task<bool> LagreType(BillettType type)
        {
            try
            {
                var funnetType = await _db.BillettTyper.SingleOrDefaultAsync(t => t.Type == type.Type);
                if (funnetType != null)
                {
                    return false;
                }
                var nyType = new BillettType
                {
                    Type = type.Type,
                    Pris = type.Pris
                };
                _db.BillettTyper.Add(nyType);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> LagreStrekning(Strekninger s)
        {
            try
            {
                var funnetStrekning = await _db.Strekninger.SingleOrDefaultAsync(sted => sted.StedNavn == s.StedNavn);
                if (funnetStrekning != null)
                {
                    return false;
                }
                var nyStrekning = new Strekninger
                {
                    StedNavn = s.StedNavn
                };
                _db.Strekninger.Add(nyStrekning);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> LagreTidsplan(Tidsplaner t)
        {
            try
            {
                var funnetTidsplan = await _db.Tidsplaner.SingleOrDefaultAsync(tid => tid.ReiseTid == t.ReiseTid && tid.Ukedag == t.Ukedag);
                if (funnetTidsplan != null)
                {
                    return false;
                }
                var nyTidsplan = new Tidsplaner
                {
                    ReiseTid = t.ReiseTid,
                    Ukedag = t.Ukedag
                };
                _db.Tidsplaner.Add(nyTidsplan);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> LagreRute(int id_sted, int id_tid)
        {
            try
            {
                var funnetStrekning = await _db.Strekninger.FindAsync(id_sted);
                var funnetTidsplan = await _db.Tidsplaner.FindAsync(id_tid);
                if (funnetTidsplan == null || funnetStrekning == null)
                {
                    return false;
                }
                var nyRute = new Rute
                {
                    StedNavn = funnetStrekning,
                    TId = funnetTidsplan
                };
                var ruteFinnes = await _db.Ruter.SingleOrDefaultAsync(r => r.StedNavn == nyRute.StedNavn && r.TId == nyRute.TId);
                if (ruteFinnes != null)
                {
                    return false;
                }
                _db.Ruter.Add(nyRute);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> SlettType(int id)
        {
            try
            {
                BillettType enType = await _db.BillettTyper.FindAsync(id);
                _db.BillettTyper.Remove(enType);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> SlettStrekning(int id)
        {
            try
            {
                Strekninger enStrekning = await _db.Strekninger.FindAsync(id);
                _db.Strekninger.Remove(enStrekning);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> SlettKunde(int id)
        {
            try
            {
                Kunder enKunde = await _db.Kunder.FindAsync(id);
                _db.Kunder.Remove(enKunde);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> SlettBillett(int id)
        {
            try
            {
                EnkeltBillett enBillett = await _db.EnkeltBilletter.FindAsync(id);
                _db.EnkeltBilletter.Remove(enBillett);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> SlettBestilling(int id)
        {
            try
            {
                Bestillingen enBestilling = await _db.Bestillinger.FindAsync(id);
                _db.Bestillinger.Remove(enBestilling);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> SlettRute(int id)
        {
            try
            {
                Rute enRute = await _db.Ruter.FindAsync(id);
                _db.Ruter.Remove(enRute);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> SlettTidsplan(int id)
        {
            try
            {
                Tidsplaner enTidsplan = await _db.Tidsplaner.FindAsync(id);
                _db.Tidsplaner.Remove(enTidsplan);
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> EndreType(int id, BillettType obj)
        {
            try
            {
                BillettType enType = await _db.BillettTyper.FindAsync(id);
                enType.Type = obj.Type;
                enType.Pris = obj.Pris;
                var typeFinnes = await _db.BillettTyper.SingleOrDefaultAsync(t => t.Type == enType.Type && t.PId != enType.PId);
                if (typeFinnes != null)
                {
                    return false;
                }
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> EndreStrekning(Strekninger obj)
        {
            try
            {
                Strekninger funnet = await _db.Strekninger.FindAsync(obj.SId);
                funnet.StedNavn = obj.StedNavn;
                var strekningFinnes = await _db.Strekninger.SingleOrDefaultAsync(s => s.StedNavn == funnet.StedNavn && s.SId != funnet.SId);
                if (strekningFinnes != null)
                {
                    return false;
                }
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> EndreKunde(Kunder obj)
        {
            try
            {
                Kunder funnet = await _db.Kunder.FindAsync(obj.KId);
                funnet.Epost = obj.Epost;
                funnet.Telefon = obj.Telefon;
                var epostFinnes = await _db.Kunder.SingleOrDefaultAsync(k => k.Epost == funnet.Epost && k.KId != funnet.KId);
                if (epostFinnes != null)
                {
                    return false;
                }
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> EndreTidsplan(Tidsplaner obj)
        {
            try
            {
                Tidsplaner funnet = await _db.Tidsplaner.FindAsync(obj.TId);
                funnet.ReiseTid = obj.ReiseTid;
                funnet.Ukedag = obj.Ukedag;
                var dataFinnes = await _db.Tidsplaner.SingleOrDefaultAsync(t => t.ReiseTid == funnet.ReiseTid && t.Ukedag == funnet.Ukedag && t.TId != funnet.TId);
                if (dataFinnes != null)
                {
                    return false;
                }
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }

        public async Task<bool> EndreRute(int id, int id_sted, int id_tid)
        {
            try
            {
                Rute funnetRute = await _db.Ruter.FindAsync(id);
                Strekninger funnetStrekning = await _db.Strekninger.FindAsync(id_sted);
                Tidsplaner funnetTidsplan = await _db.Tidsplaner.FindAsync(id_tid);
                funnetRute.StedNavn = funnetStrekning;
                funnetRute.TId = funnetTidsplan;
                var dataFinnes = await _db.Ruter.SingleOrDefaultAsync(r => r.StedNavn == funnetRute.StedNavn && r.TId == funnetRute.TId && r.Rute_Id != funnetRute.Rute_Id);
                if (dataFinnes != null)
                {
                    return false;
                }
                await _db.SaveChangesAsync();
                return true;
            }
            catch (Exception e)
            {
                _log.LogInformation(e.Message);
                return false;
            }
        }
    }
}
