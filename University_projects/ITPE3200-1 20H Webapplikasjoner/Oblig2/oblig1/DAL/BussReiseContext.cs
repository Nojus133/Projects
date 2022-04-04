using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace oblig1.DAL
{
    public class Kunder
    {
        [Key]
        public int KId { get; set; }
        //[DatabaseGenerated(DatabaseGeneratedOption.None)]
        public string Epost { get; set; }
        public string Telefon { get; set; }
        //public virtual Bestilling Bestillinger { get; set; }
    }

    public class Bestillingen
    {
        [Key]
        public int BId { get; set; }
        public DateTime BestillingsDato { get; set; }
        public int TotaltPris { get; set; }

        [Required]
        public virtual Kunder Kunder { get; set; }
        
    }

    public class BillettType
    {
        [Key]
        public int PId { get; set; }
        //[DatabaseGenerated(DatabaseGeneratedOption.None)]
        public string Type { get; set; }
        public int Pris { get; set; }

    }

    public class Tidsplaner
    {
        [Key]
        public int TId { get; set; }
        public string ReiseTid { get; set; }
        public int Ukedag { get; set; }
    }

    public class Strekninger
    {
        [Key]
        public int SId { get; set; }
        //[DatabaseGenerated(DatabaseGeneratedOption.None)]
        public string StedNavn { get; set; }
    }

    public class Rute
    {
        [Key]
        public int Rute_Id { get; set; }

        [Required]
        public virtual Strekninger StedNavn { get; set; }
        [Required]
        public virtual Tidsplaner TId { get; set; }
    }

    public class EnkeltBillett
    {

        [Key]
        public int BillettNr { get; set; }
        public DateTime ReiseDato { get; set; }

        [Required]
        public virtual Bestillingen Bestillinger { get; set; }
        [Required]
        public virtual BillettType BillettTyper { get; set; }
        [Required]
        public virtual Strekninger Fra { get; set; }
        [Required]
        public virtual Strekninger Til { get; set; }
        [Required]
        public virtual Rute Ruten { get; set; }
    }

    public class Brukere
    {
        public int Id { get; set; }
        public string Brukernavn { get; set; }
        public byte[] Passord { get; set; }
        public byte[] Salt { get; set; }
    }



    public class BussReiseContext : DbContext
    {
        public BussReiseContext (DbContextOptions<BussReiseContext> options) : base(options)
        {
            Database.EnsureCreated();
        }

        public DbSet<Kunder> Kunder { get; set; }

        public DbSet<Bestillingen> Bestillinger { get; set; }

        public DbSet<BillettType> BillettTyper { get; set; }

        public DbSet<Tidsplaner> Tidsplaner { get; set; }

        public DbSet<Strekninger> Strekninger { get; set; }

        public DbSet<Rute> Ruter { get; set; }

        public DbSet<EnkeltBillett> EnkeltBilletter { get; set; }

        public DbSet<Brukere> Brukere { get; set; }


        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseLazyLoadingProxies();
        }
    }
}
