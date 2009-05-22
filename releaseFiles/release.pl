#!/usr/bin/perl

$telescope = shift or die "No telescope(s) { ukirt | jcmt | both } \n" ;

$telescope = lc( $telescope ) ;
@telescopes = ( 'ukirt' , 'jcmt' , 'both' ) ;
$match = 0 ;
foreach $scope ( @telescopes ){ if( $telescope eq $scope ){ $match = 1 ; } }
if( $match == 0 ){ die "\'$telescope\' not in { @telescopes }\n" ; }

@telescopes = () ;

if( $telescope eq 'ukirt' || $telescope eq 'both' )
{
	push( @telescopes , 'ukirt' ) ;
}
if( $telescope eq 'jcmt' || $telescope eq 'both' )
{ 
	push( @telescopes , 'jcmt' ) ;
}
else
{ 
	die "$telescope does not appear to be ukirt or jcmt or both\n" ; 
}

chomp( $cwd = `pwd` ) ;

chomp( $os = `uname -s` ) ;

die "Couldn't build release.\n" 
if system( 'perl' , ( "$cwd/win-nix-release.pl" , $telescope ) ) ;

foreach $scope ( @telescopes )
{
	if( $os eq 'Darwin' )
	{
		die "Couldn't create MacOS release.\n" if system( 'perl' , ( "$cwd/macosx-release.pl" , $scope ) ) ;
	}
	else
	{
		print "Skipping MacOS X build for $scope.\n" ;
	}
}

die "Problems running ftp script.\n" 
if system( 'perl' , ( "$cwd/ftp.pl" , $telescope ) ) ;
