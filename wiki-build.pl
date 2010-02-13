$cwd = `pwd` ;
chomp( $cwd ) ;

@build_order = ( 'GEMINI' , 'ORAC' , 'OMP' , 'EDFREQ' , 'OT' ) ;
%tools = () ;
$tools{ 'ORAC' } = [ "$cwd/ORAC/tools" ] ;
$tools{ 'OMP' } = [ "$cwd/OMP/tools" ] ;
$tools{ 'EDFREQ' } = [ "$cwd/ORAC/tools" ] ;
$tools{ 'OT' } = [ "$cwd/OT/tools" , "$cwd/ORAC/tools" , "$cwd/OMP/tools" ] ;

$local_install_dir = '../install' ;
$local_install_path = "$local_install_dir/classes" ;
$local_jar_dir = "$local_install_dir/lib" ;
@local_tools_dir = () ;
$jar_file = '' ;
$classpath = $local_install_path ;
$install_dir = "$cwd/install" ;

$javac = `which javac` ;
chomp( $javac ) ;
$jar = `which jar` ;
chomp( $jar ) ;


&main ;

sub main
{
	&clean ;

	foreach $package ( @build_order )
	{
		$to_lower_case = lc( $package ) ;
		$jar_file = "$to_lower_case.jar" ;
		$array_reference = $tools{ $package } ;
		if( defined $array_reference )
		{
			@local_tools_dir = @{ $array_reference } ;
		}
		else
		{
			@local_tools_dir = () ;
		}
		&build( $package ) ;
		$classpath .= ":$cwd/$package/install/lib/$jar_file" ;
	}

	&install ;
}

sub build
{
	$package = shift or die "No argument\n" ;
	print "-" x 20 . "\n" . "Building $package \n" ;
	if( -e "$package/src" )
	{
		chdir "$package/src" or die "Could not cd to $package/src \n" ;
		&all ;
		chdir $cwd or die "Could not cd to $cwd \n" ;
	}
}

sub all
{
	&local_mkdir ;
	&javac ;
	&jar ;
}

sub clean
{
	foreach $src_dir ( @build_order )
	{
		chdir "$cwd/$src_dir/src"
		or die "Could not cd to $src_dir/src \n" ;
		&local_clean ;
		chdir $cwd or die "Could not cd to $cwd \n" ;
	}
	if( -e $install_dir ){ `rm -rf $install_dir` ; }
	if( -e "$cwd/OT/cfg/jcmt/versionFile" )
	{ `rm -f $cwd/OT/cfg/jcmt/versionFile` ; }
	if( -e "$cwd/OT/cfg/ukirt/versionFile" )
	{ `rm -f $cwd/OT/cfg/ukirt/versionFile` ; }
	if( -e "$cwd/OT/cfg/jcmt/tau.list" )
	{ `rm -f $cwd/OT/cfg/jcmt/tau.list` ; }
	print "Cleaned\n" ;
}

sub local_clean
{
	if( -e $local_install_path )
	{ `rm -rf $local_install_path` ; }
	if( -e $local_jar_dir )
	{ `rm -rf $local_jar_dir` ; }
	if( -e $local_install_dir )
	{ `rm -rf $local_install_dir` ; }
	print "Cleaned " . `pwd` ;
}

sub local_mkdir
{
	unless( -e $local_install_dir )
	{ `mkdir -p $local_install_dir` ; }
	unless( -e $local_install_path )
	{ `mkdir -p $local_install_path` ; }
	unless( -e $local_jar_dir )
	{ `mkdir -p $local_jar_dir` ; }
	print "Directories created for " . `pwd` ;
}

sub javac
{
	print "Compiling " . `pwd` ;

	@source_files = () ;
	@files = `find . -name "*.java"` ;
	foreach $file ( @files )
	{
		chomp( $file ) ;
		$file = substr $file , 2 ;
		push( @source_files , $file ) ;
	}

	$local_classpath = $classpath ;
	foreach $dir ( @local_tools_dir )
	{
		@tools = `find $dir -name "*.jar"` ;
		foreach $tool ( @tools )
		{
			chomp( $tool ) ;
			$local_classpath .= ":$tool" ;
		}
	}

	@compile_args = () ;
	push( @compile_args , '-target' , '1.6' ) ;
	push( @compile_args , '-d' , $local_install_path ) ;
	push( @compile_args , '-classpath' , $local_classpath ) ;
	push( @compile_args , '-sourcepath' , '.' ) ;
	push( @compile_args , @source_files ) ;

	unless( system( $javac , @compile_args ) )
	{
		print "Compiled " . @source_files . " files\n" ;
	}
	else
	{
		die "Problems compiling\n" ;
	}

	@images = `find . -name images` ;
	foreach $image_folder ( @images )
	{
		chomp( $image_folder ) ;
		$image_dir = substr $image_folder , 2 ;
		$image_dir =~ s/images// ;
		$image_dir = "$local_install_path/$image_dir" ;
		if( system( 'cp' , ( '-r' , $image_folder , $image_dir ) ) )
		{
			die "Couldn't copy $image_folder to $image_dir" ;
		}
	}
}

sub jar
{
	@jar_args = () ;
	push( @jar_args , 'cf' , "$local_jar_dir/$jar_file" ) ;
	push( @jar_args , '-C' , $local_install_path ) ;
	push( @jar_args , '.' ) ;
	unless( system( $jar , @jar_args ) )
	{
		print "Jar'd\n" ;
	}
	else
	{
		die "Problems Jar'ing" ;
	}
}

sub make_tau_file
{
	chdir "$cwd/OT/cfg/jcmt"
	or die "Could not cd to $cwd/OT/cfg/jcmt \n" ;
	`ls tau*.dat > tau.list` ;
	chdir "$cwd" or die "Could not cd to $cwd \n" ;
}

sub make_versionFiles
{
	$date = `date '+%Y%m%d'` ;
	chomp( $date ) ;
	$version = `git log | head -n 1 | cut -f2 -d ' '` ;
	chomp( $version ) ;
	`echo $date [$version] > $cwd/OT/cfg/jcmt/versionFile` ;
	`echo $date [$version] > $cwd/OT/cfg/ukirt/versionFile` ;
}

sub install
{
	print "Installing ... \n" ;
	if( -e $install_dir ){ die "$install_dir already exists \n" ; }
	if( system( 'mkdir' , ( $install_dir ) ) )
	{
		die "Unable to create $install_dir \n" ;
	}

	&make_versionFiles ;
	&make_tau_file ;
	&copy_jars ;
	&copy_cfgs ;
	&cfg_jar ;
	&shell_scripts ;
}

sub cfg_jar
{
	chdir "$install_dir/cfg" or die "Could not cd to $install_dir/cfg \n" ;
	`$jar cf $install_dir/lib/cfg.jar .` ;
	chdir $cwd or die "Could not cd to $cwd \n" ;
}

sub copy_cfgs
{
	if( system( 'mkdir' , ( '-p' , "$install_dir/cfg/ot" ) ) )
	{
		die "Unable to create $install_dir/cfg/ot \n" ;
	}
	@files = `ls $cwd/OT/cfg` ;
	foreach $file ( @files )
	{
		chomp( $file ) ;
		`cp -r $cwd/OT/cfg/$file $install_dir/cfg/ot` ;
	}
	`cp -r $cwd/EDFREQ/cfg/edfreq $install_dir/cfg/ot/jcmt` ;
}

sub copy_jars
{
	if( system( 'mkdir' , ( "$install_dir/lib" ) ) )
        {
                die "Unable to create $install_dir/lib \n" ;
        }
	if( system( 'mkdir' , ( "$install_dir/tools" ) ) )
	{
		die "Unable to create $install_dir/tools \n" ;
	}

	foreach $package ( @build_order )
	{
		$to_lowercase = lc( $package ) ;
		@cp_args = () ;
		push( @cp_args , "$cwd/$package/install/lib/$to_lowercase.jar" ) ;
		push( @cp_args , "$install_dir/lib" ) ;
		if( -e $cp_args[ 0 ] )
		{
			if( system( 'cp' , @cp_args ) )
			{
				die "Could not copy $cp_args[ 0 ] \n" ;
			}
		}

		$array_reference = $tools{ $package } ;
		if( defined $array_reference )
		{
			@local_tools_dir = @{ $array_reference } ;
		}
		else
		{
			@local_tools_dir = () ;
		}
		foreach $dir ( @local_tools_dir )
		{
			@tools = `ls $dir` ;
			foreach $tool ( @tools )
			{
				chomp( $tool ) ;
				`cp -r $dir/$tool $install_dir/tools` ;
			}
		}
	}
}

sub shell_scripts
{
	if( system( 'mkdir' , ( "$install_dir/bin" ) ) )
        {
                die "Unable to create $install_dir/bin \n" ;
        }

	# *NIX C SHELL SCRIPT
	open( HANDLE , '<' , "$cwd/OT/src/scripts/ot_script_source" ) ;
	@lines = <HANDLE> ;
	close HANDLE ;

	$runtime_classpath = '' ;

	chdir "$install_dir/bin" or die "Could not cd to $install_dir/bin \n" ;
	@files = `find ../ -name "*.jar"` ;
	@jars = () ;
	foreach $file ( @files )
	{
		chomp( $file ) ;
		push( @jars , $file ) ;
	}
	$runtime_classpath = join( ':' , @jars ) ;

	open( HANDLE , '>' , "$install_dir/bin/ot" ) ;
	print HANDLE "#!/bin/csh -f\n\n" ;
	print HANDLE "cd `dirname \$0`\n\n" ;
	print HANDLE "set CLASSPATH = $runtime_classpath\n\n" ;

	foreach $line ( @lines ){ print HANDLE $line ; }

	close HANDLE ;

	`chmod +x $install_dir/bin/ot` ;

	# WINDOWS BATCH FILE
	open( HANDLE , '<' , "$cwd/OT/src/scripts/ot_bat_install_all_source" ) ;
	@lines = <HANDLE> ;
	close HANDLE ;

	$runtime_classpath =~ s/\//\\/g ;
	$runtime_classpath =~ s/:/;/g ;

	open( HANDLE , '>' , "$install_dir/bin/ot.bat" ) ;
	foreach $line ( @lines )
	{
		if( $line =~ /set CLASSPATH/ )
		{
			print HANDLE "set CLASSPATH=$runtime_classpath \n" ;
		}
		else
		{
			print HANDLE $line ;
		}
	}
	close HANDLE ;
}
